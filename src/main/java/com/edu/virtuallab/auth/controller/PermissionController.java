package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.Permission;
import com.edu.virtuallab.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public Permission getById(@PathVariable Long id) {
        return permissionService.getById(id);
    }

    @GetMapping("/code/{code}")
    public Permission getByCode(@PathVariable String code) {
        return permissionService.getByCode(code);
    }

    @GetMapping("/list")
    public List<Permission> listAll() {
        return permissionService.listAll();
    }

    @PostMapping("/create")
    public boolean create(@RequestBody Permission permission) {
        return permissionService.create(permission);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return permissionService.delete(id);
    }
} 