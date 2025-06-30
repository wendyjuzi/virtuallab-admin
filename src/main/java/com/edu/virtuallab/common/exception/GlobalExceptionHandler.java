package com.edu.virtuallab.common.exception;

import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e) {
        e.printStackTrace();
        return CommonResult.failed(e.getMessage());
    }
} 