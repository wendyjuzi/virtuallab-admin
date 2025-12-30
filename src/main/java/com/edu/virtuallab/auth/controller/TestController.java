package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {
    
    @PutMapping("/put-test")
    @ApiOperation("测试PUT请求")
    public CommonResult<Map<String, Object>> testPut(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "PUT请求成功");
        result.put("data", data);
        return CommonResult.success(result, "资源更新成功");
    }
    
    @PostMapping("/post-test")
    @ApiOperation("测试POST请求")
    public CommonResult<Map<String, Object>> testPost(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "POST请求成功");
        result.put("data", data);
        return CommonResult.success(result, "资源更新成功");
    }
    
    @GetMapping("/get-test")
    @ApiOperation("测试GET请求")
    public CommonResult<Map<String, Object>> testGet() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "GET请求成功");
        return CommonResult.success(result, "资源更新成功");
    }
    
    @DeleteMapping("/delete-test")
    @ApiOperation("测试DELETE请求")
    public CommonResult<Map<String, Object>> testDelete() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "DELETE请求成功");
        return CommonResult.success(result, "资源更新成功");
    }
} 