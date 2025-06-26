package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int delete(Long id);
} 