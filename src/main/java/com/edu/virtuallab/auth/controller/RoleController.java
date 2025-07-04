package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public Role getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping("/name/{name}")
    public Role getByName(@PathVariable String name) {
        return roleService.getByName(name);
    }

    @GetMapping("/list")
    public List<Role> listAll() {
        return roleService.listAll();
    }

    @OperationLogRecord(operation = "CREATE_ROLE", module = "ROLE", action = "创建角色", description = "管理员创建角色", permissionCode = "ROLE_MANAGE")
    @PostMapping("/create")
    public boolean create(@RequestBody Role role) {
        return roleService.create(role);
    }

    @OperationLogRecord(operation = "UPDATE_ROLE", module = "ROLE", action = "更新角色", description = "管理员更新角色", permissionCode = "ROLE_MANAGE")
    @PutMapping("/update")
    public boolean update(@RequestBody Role role) {
        return roleService.update(role);
    }

    @OperationLogRecord(operation = "DELETE_ROLE", module = "ROLE", action = "删除角色", description = "管理员删除角色", permissionCode = "ROLE_MANAGE")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return roleService.delete(id);
    }

    @PostMapping("/assignPermissions")
    public boolean assignPermissions(@RequestParam Long roleId, @RequestBody List<Long> permissionIds) {
        return roleService.assignPermissions(roleId, permissionIds);
    }
} 