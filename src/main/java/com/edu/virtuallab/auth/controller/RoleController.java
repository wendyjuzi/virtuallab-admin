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

@RestController
@RequestMapping("/api/role")
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

    @PostMapping("/create")
    public boolean create(@RequestBody Role role) {
        return roleService.create(role);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody Role role) {
        return roleService.update(role);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return roleService.delete(id);
    }

    @PostMapping("/assignPermissions")
    public boolean assignPermissions(@RequestParam Long roleId, @RequestBody List<Long> permissionIds) {
        return roleService.assignPermissions(roleId, permissionIds);
    }
} 