package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentDao {
    
    /**
     * 插入院系
     */
    int insert(Department department);
    
    /**
     * 更新院系
     */
    int update(Department department);
    
    /**
     * 根据ID删除院系
     */
    int deleteById(Long id);
    
    /**
     * 根据ID查询院系
     */
    Department findById(Long id);
    
    /**
     * 根据编码查询院系
     */
    Department findByCode(String code);
    
    /**
     * 查询所有院系
     */
    List<Department> findAll();
    
    /**
     * 根据父ID查询子院系
     */
    List<Department> findByParentId(Long parentId);
    
    /**
     * 根据级别查询院系
     */
    List<Department> findByLevel(Integer level);
    
    /**
     * 根据状态查询院系
     */
    List<Department> findByStatus(Integer status);
    
    /**
     * 分页查询院系
     */
    List<Department> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    /**
     * 统计院系总数
     */
    int count();
} 