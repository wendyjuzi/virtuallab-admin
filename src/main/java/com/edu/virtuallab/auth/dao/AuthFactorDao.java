package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.AuthFactor;
import java.util.List;

public interface AuthFactorDao {
    List<AuthFactor> findByUserId(Long userId);
    int insert(AuthFactor authFactor);
    int update(AuthFactor authFactor);
    int delete(Long id);
} 