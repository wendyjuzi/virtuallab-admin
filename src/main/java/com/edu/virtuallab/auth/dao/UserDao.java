package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    
    // 基础CRUD操作
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int delete(@Param("id") Long id);
    
    // 扩展查询方法
    User findByPhone(@Param("phone") String phone);
    User findByEmail(@Param("email") String email);
    User findByStudentId(@Param("studentId") String studentId);
    
    // 根据院系查询用户
    List<User> findByDepartment(@Param("department") String department);
    
    // 根据专业查询用户
    List<User> findByMajor(@Param("major") String major);
    
    // 根据班级查询用户
    List<User> findByClassName(@Param("className") String className);
    
    // 根据状态查询用户
    List<User> findByStatus(@Param("status") Integer status);
    
    // 分页查询
    List<User> findByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    // 条件查询
    List<User> findByCondition(@Param("username") String username, 
                              @Param("realName") String realName,
                              @Param("department") String department,
                              @Param("status") Integer status);
    
    // 更新用户状态
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    // 更新登录信息
    int updateLoginInfo(@Param("id") Long id, 
                       @Param("lastLoginTime") String lastLoginTime,
                       @Param("lastLoginIp") String lastLoginIp,
                       @Param("loginAttempts") Integer loginAttempts);
    
    // 锁定用户
    int lockUser(@Param("id") Long id, @Param("lockTime") String lockTime);
    
    // 解锁用户
    int unlockUser(@Param("id") Long id);
    
    // 重置登录尝试次数
    int resetLoginAttempts(@Param("id") Long id);
    
    // 更新指纹数据
    int updateFingerprint(@Param("id") Long id, @Param("fingerprint") String fingerprint);
} 