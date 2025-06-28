package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import java.util.List;

public interface UserService {
    User getById(Long id);
    User getByUsername(String username);
    List<User> listAll();
    boolean register(UserRegisterDTO dto);
    boolean update(User user);
    boolean delete(Long id);
    boolean assignRoles(Long userId, List<Long> roleIds);
} 