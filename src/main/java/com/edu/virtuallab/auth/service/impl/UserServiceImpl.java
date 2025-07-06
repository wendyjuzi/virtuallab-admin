package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.RoleDao;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.dao.UserRoleDao;
import com.edu.virtuallab.auth.dao.EmailVerificationCodeDao;
import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import com.edu.virtuallab.auth.model.UserRole;
import com.edu.virtuallab.auth.service.AuthFactorService;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AuthFactorService authFactorService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private org.springframework.data.redis.core.StringRedisTemplate redisTemplate;
    @Autowired
    private com.edu.virtuallab.auth.dao.EmailVerificationCodeDao emailVerificationCodeDao;

    // ==================== 基础CRUD操作 ====================

    @Override
    public User getById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> listAll() {
        return userDao.findAll();
    }

    @Override
    public PageResult<User> getUserList(String username, String realName, String department,
                                       String userType, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<User> users = userDao.findByConditions(username, realName, department, userType, status, offset, size);
        int total = userDao.countByConditions(username, realName, department, userType, status);
        // 为每个用户补全角色列表
        for (User user : users) {
            List<Role> roles = roleDao.selectRolesByUserId(user.getId());
            user.setRoleList(roles);
        }
        return new PageResult<>(total, users);
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        // 只校验用户名唯一
        if (isUsernameExists(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(User.STATUS_NORMAL);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userDao.insert(user) > 0;
    }

    @Override
    public void register(UserRegisterDTO dto) {
        // 仅管理员注册需要校验邮箱验证码
        if ("admin".equalsIgnoreCase(dto.getUserType())) {
            String cacheCode = redisTemplate.opsForValue().get("email:code:" + dto.getEmail());
            boolean valid = false;
            if (cacheCode != null && cacheCode.equals(dto.getCode())) {
                valid = true;
            } else {
                // 数据库校验
                int count = emailVerificationCodeDao.countValidCode(dto.getEmail(), dto.getCode(), new java.util.Date());
                valid = count > 0;
            }
            if (!valid) {
                throw new RuntimeException("邮箱验证码错误或已过期");
            }
        }
        // 邮箱唯一性校验
        if (existsByEmail(dto.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        userDao.insert(dto.toUser());
    }

    @Override
    @Transactional
    public boolean update(User user) {
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        // 删除用户角色关联
        userRoleDao.deleteByUserId(id);
        // 删除用户
        return userDao.delete(id) > 0;
    }

    // ==================== 个人信息管理 ====================

    @Override
    public User getCurrentUserProfile(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    @Transactional
    public boolean updateProfile(User user) {
        // 不再校验邮箱、手机号、学号唯一性
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    public boolean updateAvatar(Long userId, String avatarUrl) {
        User user = new User();
        user.setId(userId);
        user.setUpdateTime(new Date());
        // 这里需要在User模型中添加avatar字段
        // user.setAvatar(avatarUrl);
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());

        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean resetPassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());

        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean updateEmail(Long userId, String newEmail, String verificationCode) {
        // 验证邮箱验证码
        if (!authFactorService.validateEmailCode(userId, newEmail, verificationCode)) {
            throw new BusinessException("邮箱验证码错误");
        }

        // 检查邮箱是否被其他用户使用
        User existingUser = userDao.findByEmail(newEmail);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new BusinessException("邮箱已被其他用户使用");
        }

        User user = new User();
        user.setId(userId);
        user.setEmail(newEmail);
        user.setUpdateTime(new Date());

        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean updatePhone(Long userId, String newPhone, String verificationCode) {
        throw new UnsupportedOperationException("已移除手机号相关功能");
    }

    // ==================== 用户状态管理 ====================

    @Override
    @Transactional
    public boolean enableUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(User.STATUS_NORMAL);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean disableUser(Long userId, String reason) {
        User user = new User();
        user.setId(userId);
        user.setStatus(User.STATUS_DISABLED);
        user.setUpdateTime(new Date());
        // 这里可以在User模型中添加disableReason字段
        // user.setDisableReason(reason);
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean lockUser(Long userId, String reason) {
        User user = new User();
        user.setId(userId);
        user.setStatus(User.STATUS_LOCKED);
        user.setUpdateTime(new Date());
        // 这里可以在User模型中添加lockReason字段
        // user.setLockReason(reason);
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean unlockUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(User.STATUS_NORMAL);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    // ==================== 角色权限管理 ====================

    @Override
    @Transactional
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 先删除现有角色
        userRoleDao.deleteByUserId(userId);

        // 分配新角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleDao.insert(userRole);
        }
        return true;
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        List<UserRole> userRoles = userRoleDao.findByUserId(userId);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean removeUserRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            userRoleDao.deleteByUserIdAndRoleId(userId, roleId);
        }
        return true;
    }

    // ==================== 查询统计 ====================

    @Override
    public List<User> findUsersByCondition(String username, String realName, String department,
                                          String userType, Integer status) {
        return userDao.findByConditions(username, realName, department, userType, status, 0, Integer.MAX_VALUE);
    }

    @Override
    public int countUsers(String username, String realName, String department,
                         String userType, Integer status) {
        return userDao.countByConditions(username, realName, department, userType, status);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userDao.findByUsername(username) != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return userDao.findByPhone(phone) != null;
    }

    @Override
    public boolean isStudentIdExists(String studentId) {
        return userDao.findByStudentId(studentId) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.countByEmail(email) > 0;
    }

    @Override
    public User getByEmail(String email) {
        return userDao.findByEmail(email);
    }


    @Override
    public User findByStudentId(String studentId){return userDao.findByStudentId(studentId);}

    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }
        return userDao.findByIds(userIds);
    }
}