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

    // 在 UserDao 接口中添加以下方法：

    // 查询所有管理员用户
    List<User> findAdmins();

    // 根据班级ID列表查询用户ID
    List<Long> findUserIdsByClassIds(@Param("classIds") List<Long> classIds);

    // 新增
    int deleteById(Long id);

    List<User> findByDepartmentId(Long departmentId);

    List<User> findByConditions(@Param("username") String username,
                               @Param("realName") String realName,
                               @Param("department") String department,
                               @Param("userType") String userType,
                               @Param("status") Integer status,
                               @Param("offset") int offset,
                               @Param("size") int size);

    int countByConditions(@Param("username") String username,
                         @Param("realName") String realName,
                         @Param("department") String department,
                         @Param("userType") String userType,
                         @Param("status") Integer status);

    /**
     * 根据院系名称统计用户数量
     */
    int countByDepartment(@Param("department") String department);
    
    /**
     * 统计活跃用户数量（最近指定时间登录的用户）
     */
    int countActiveUsers(@Param("since") java.util.Date since);
    
    /**
     * 统计今日登录数量
     */
    int countTodayLogins(@Param("todayStart") java.util.Date todayStart);

    /**
     * 根据院系和角色统计学生数量
     */
    int countStudentsByDepartment(@Param("department") String department);
    /**
     * 根据院系和角色统计老师数量
     */
    int countTeachersByDepartment(@Param("department") String department);
}