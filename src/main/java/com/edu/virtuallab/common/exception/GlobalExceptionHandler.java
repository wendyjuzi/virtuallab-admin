package com.edu.virtuallab.common.exception;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.ResultCode;
import com.edu.virtuallab.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("访问被拒绝: {}", e.getMessage());
        // 修改：使用构造方法创建结果对象
        return new CommonResult<>(
                ResultCode.FORBIDDEN.getCode(),
                "没有访问权限",
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return CommonResult.failed("系统异常，请联系管理员");
    }
}