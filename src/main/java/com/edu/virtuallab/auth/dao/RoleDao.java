package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao {
    
    // 基础CRUD操作
    Role findById(@Param("id") Long id);
    Role findByName(@Param("name") String name);
    List<Role> findAll();
    int insert(Role role);
    int update(Role role);
    int delete(@Param("id") Long id);
    
    // 扩展查询方法
    Role findByCode(@Param("code") String code);
    
    // 根据类型查询角色
    List<Role> findByType(@Param("type") Integer type);
    
    // 根据级别查询角色
    List<Role> findByLevel(@Param("level") Integer level);
    
    // 根据状态查询角色
    List<Role> findByStatus(@Param("status") Integer status);
    
    // 查询系统角色
    List<Role> findSystemRoles();
    
    // 查询自定义角色
    List<Role> findCustomRoles();
    
    // 根据父角色ID查询子角色
    List<Role> findByParentId(@Param("parentId") Long parentId);
    
    // 分页查询
    List<Role> findByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    // 条件查询
    List<Role> findByCondition(@Param("name") String name, 
                              @Param("code") String code,
                              @Param("type") Integer type,
                              @Param("status") Integer status);
    
    // 更新角色状态
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    // 查询用户的所有角色
    List<Role> findByUserId(@Param("userId") Long userId);
    
    // 新增
    int deleteById(Long id);
    
    // 根据ID列表批量查询角色
    List<Role> findByIds(@Param("ids") List<Long> ids);
    
    // 根据院系ID查询角色
    List<Role> findByDepartmentId(@Param("departmentId") Long departmentId);

    List<Role> selectRolesByUserId(@Param("userId") Long userId);
} 