package com.tg.blog.backend.handler;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.common.exception.ArticleException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中的异常，返回标准化的错误响应
 * 
 * @author TG
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.failure(500, ex.getMessage());
    }
}
