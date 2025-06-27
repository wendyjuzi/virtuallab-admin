package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/list")
    public List<User> listAll() {
        return userService.listAll();
    }

    @PostMapping("/register")
    public CommonResult<?> register(@RequestBody UserRegisterDTO dto) {
        boolean success = userService.register(dto);
        if (success) {
            return CommonResult.success("注册成功");
        } else {
            return CommonResult.failed("注册失败");
        }
    }

    @PutMapping("/update")
    public boolean update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return userService.delete(id);
    }
} 