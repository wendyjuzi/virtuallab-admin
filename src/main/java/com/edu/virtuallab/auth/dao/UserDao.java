package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.User;
import java.util.List;

public interface UserDao {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int delete(Long id);
} 