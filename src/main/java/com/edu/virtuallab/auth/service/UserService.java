package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import com.edu.virtuallab.common.api.PageResult;
import java.util.List;

public interface UserService {
    
    // ==================== 基础CRUD操作 ====================
    
    /**
     * 根据ID获取用户信息
     */
    User getById(Long id);
    
    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);
    
    /**
     * 获取所有用户列表
     */
    List<User> listAll();
    
    /**
     * 分页查询用户列表
     */
    PageResult<User> getUserList(String username, String realName, String department, 
                                String userType, Integer status, int page, int size);
    
    /**
     * 创建用户
     */
    boolean createUser(User user);
    
    /**
     * 用户注册
     */
    boolean register(UserRegisterDTO dto);
    
    /**
     * 更新用户信息
     */
    boolean update(User user);
    
    /**
     * 删除用户
     */
    boolean delete(Long id);
    
    // ==================== 个人信息管理 ====================
    
    /**
     * 获取当前用户个人信息
     */
    User getCurrentUserProfile(Long userId);
    
    /**
     * 更新个人信息
     */
    boolean updateProfile(User user);
    
    /**
     * 更新头像
     */
    boolean updateAvatar(Long userId, String avatarUrl);
    
    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    boolean resetPassword(Long userId, String newPassword);
    
    /**
     * 更新邮箱
     */
    boolean updateEmail(Long userId, String newEmail, String verificationCode);
    
    /**
     * 更新手机号
     */
    boolean updatePhone(Long userId, String newPhone, String verificationCode);
    
    // ==================== 用户状态管理 ====================
    
    /**
     * 启用用户
     */
    boolean enableUser(Long userId);
    
    /**
     * 禁用用户
     */
    boolean disableUser(Long userId, String reason);
    
    /**
     * 锁定用户
     */
    boolean lockUser(Long userId, String reason);
    
    /**
     * 解锁用户
     */
    boolean unlockUser(Long userId);
    
    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);
    
    // ==================== 角色权限管理 ====================
    
    /**
     * 为用户分配角色
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户角色列表
     */
    List<Long> getUserRoleIds(Long userId);
    
    /**
     * 移除用户角色
     */
    boolean removeUserRoles(Long userId, List<Long> roleIds);
    
    // ==================== 查询统计 ====================
    
    /**
     * 根据条件查询用户
     */
    List<User> findUsersByCondition(String username, String realName, String department, 
                                   String userType, Integer status);
    
    /**
     * 统计用户数量
     */
    int countUsers(String username, String realName, String department, 
                   String userType, Integer status);
    
    /**
     * 检查用户名是否存在
     */
    boolean isUsernameExists(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean isEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean isPhoneExists(String phone);
    
    /**
     * 检查学号/工号是否存在
     */
    boolean isStudentIdExists(String studentId);

    /**
     * 根据学号查询用户信息
     * @param studentId 学号
     * @return user
     */
    User findByStudentId(String studentId);


    /**
     * 根据邮箱获取用户信息
     */
    User getByEmail(String email);
}