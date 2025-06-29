package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.dao.UserRoleDao;
import com.edu.virtuallab.auth.dao.RoleDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRole;
import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import com.edu.virtuallab.auth.service.AuthFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    @Transactional
    public boolean register(UserRegisterDTO dto) {
        // 管理员注册需校验短信或邮箱验证码
        if ("admin".equalsIgnoreCase(dto.getUserType())) {
            boolean smsValid = false;
            boolean emailValid = false;
            if (dto.getSmsCode() != null && dto.getPhone() != null) {
                smsValid = authFactorService.validateSmsCode(null, dto.getPhone(), dto.getSmsCode());
            }
            if (dto.getEmailCode() != null && dto.getEmail() != null) {
                emailValid = authFactorService.validateEmailCode(null, dto.getEmail(), dto.getEmailCode());
            }
            if (!smsValid && !emailValid) {
                throw new RuntimeException("管理员注册需短信或邮箱验证码校验通过");
            }
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRealName(dto.getRealName());
        user.setStudentId(dto.getStudentId());
        user.setDepartment(dto.getDepartment());
        user.setMajor(dto.getMajor());
        user.setGrade(dto.getGrade());
        user.setClassName(dto.getClassName());
        user.setStatus(User.STATUS_NORMAL);
        int userResult = userDao.insert(user);
        if (userResult <= 0) {
            throw new RuntimeException("用户注册失败");
        }
        Role role = roleDao.findByCode(dto.getRoleCode());
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        int urResult = userRoleDao.insert(userRole);
        if (urResult <= 0) {
            throw new RuntimeException("用户角色绑定失败");
        }
        return true;
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user) > 0;
    }

    @Override
    public boolean delete(Long id) {
        userRoleDao.deleteByUserId(id);
        return userDao.delete(id) > 0;
    }

    @Override
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        userRoleDao.deleteByUserId(userId);
        boolean success = true;
        for (Long roleId : roleIds) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            success &= userRoleDao.insert(ur) > 0;
        }
        return success;
    }
} 