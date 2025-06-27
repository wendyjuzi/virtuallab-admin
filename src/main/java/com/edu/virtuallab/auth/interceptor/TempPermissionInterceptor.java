package com.edu.virtuallab.auth.interceptor;

import com.edu.virtuallab.auth.annotation.RequiresTempPermission;
import com.edu.virtuallab.auth.service.TempPermissionService;
import com.edu.virtuallab.common.api.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 临时权限验证拦截器
 */
@Component
public class TempPermissionInterceptor implements HandlerInterceptor {
    
    @Autowired
    private TempPermissionService tempPermissionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresTempPermission annotation = handlerMethod.getMethodAnnotation(RequiresTempPermission.class);
        
        if (annotation == null) {
            return true;
        }
        
        Long userId = getCurrentUserId(request);
        if (userId == null) {
            handleUnauthorized(response, "用户未登录");
            return false;
        }
        
        boolean hasPermission = false;
        
        if (annotation.permissionId() > 0) {
            hasPermission = tempPermissionService.hasTempPermission(userId, annotation.permissionId());
        }
        
        if (!hasPermission && annotation.roleId() > 0) {
            hasPermission = tempPermissionService.hasTempRole(userId, annotation.roleId());
        }
        
        if (!hasPermission) {
            handleForbidden(response, annotation.message());
            return false;
        }
        
        return true;
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String userIdStr = request.getHeader("X-User-ID");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        
        return null;
    }
    
    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        CommonResult<String> result = CommonResult.unauthorized(message);
        String json = objectMapper.writeValueAsString(result);
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }
    
    private void handleForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        CommonResult<String> result = CommonResult.forbidden(message);
        String json = objectMapper.writeValueAsString(result);
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }
} 