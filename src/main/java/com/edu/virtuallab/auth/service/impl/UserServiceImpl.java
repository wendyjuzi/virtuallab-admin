package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.dao.UserRoleDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRole;
import com.edu.virtuallab.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

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
    public boolean register(User user) {
        return userDao.insert(user) > 0;
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