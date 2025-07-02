package com.edu.virtuallab.log.aspect;

import com.edu.virtuallab.log.annotation.OperationLogRecord;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private OperationLogService operationLogService;

    @Around("@annotation(com.edu.virtuallab.log.annotation.OperationLogRecord)")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Exception ex = null;
        long start = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLogRecord record = method.getAnnotation(OperationLogRecord.class);
            // 获取用户信息（可根据实际项目调整）
            Long userId = null;
            String username = null;
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                // 假设token里有用户名/ID，或session里有
                Object uid = request.getAttribute("userId");
                Object uname = request.getAttribute("username");
                if (uid instanceof Long) userId = (Long) uid;
                if (uname instanceof String) username = (String) uname;
                // 你可以根据实际登录逻辑获取用户信息
            }
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setUsername(username);
            log.setOperation(record.operation());
            log.setPermissionCode(record.permissionCode());
            log.setModule(record.module());
            log.setAction(record.action());
            log.setDescription(record.description());
            log.setStatus(ex == null ? 1 : 0);
            log.setCreateTime(new Date());
            log.setExecutionTime(end - start);
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                log.setIpAddress(request.getRemoteAddr());
                log.setUserAgent(request.getHeader("User-Agent"));
            }
            operationLogService.log(log);
        }
    }
} 