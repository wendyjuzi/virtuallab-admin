package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.Permission;
import com.edu.virtuallab.auth.service.PermissionService;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permission")
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

    @OperationLogRecord(operation = "CREATE_PERMISSION", module = "PERMISSION", action = "创建权限", description = "管理员创建权限", permissionCode = "PERMISSION_MANAGE")
    @PostMapping("/create")
    public boolean create(@RequestBody Permission permission) {
        return permissionService.create(permission);
    }

    @OperationLogRecord(operation = "UPDATE_PERMISSION", module = "PERMISSION", action = "更新权限", description = "管理员更新权限", permissionCode = "PERMISSION_MANAGE")
    @PutMapping("/update")
    public boolean update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }

    @OperationLogRecord(operation = "DELETE_PERMISSION", module = "PERMISSION", action = "删除权限", description = "管理员删除权限", permissionCode = "PERMISSION_MANAGE")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return permissionService.delete(id);
    }
} 