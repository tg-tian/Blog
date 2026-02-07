package com.tg.blog.backend.handler;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.common.exception.ArticleException;
import com.tg.blog.backend.common.exception.LoginBadCredentialsException;
import com.tg.blog.backend.common.exception.LoginParameterException;
import com.tg.blog.backend.common.exception.LoginServiceException;
import com.tg.blog.backend.common.exception.LoginUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中的异常，返回标准化的错误响应
 * 
 * @author TG
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 登录参数不合法：400
    @ExceptionHandler(LoginParameterException.class)
    public ResponseEntity<?> handleLoginParameter(LoginParameterException ex) {
        log.warn("登录参数异常: {}", ex.getMessage());
        return ResponseEntity.failure(400, ex.getMessage());
    }

    // 用户不存在：404
    @ExceptionHandler(LoginUserNotFoundException.class)
    public ResponseEntity<?> handleLoginUserNotFound(LoginUserNotFoundException ex) {
        log.warn("用户不存在: {}", ex.getMessage());
        return ResponseEntity.failure(404, ex.getMessage());
    }

    // 密码错误：401
    @ExceptionHandler(LoginBadCredentialsException.class)
    public ResponseEntity<?> handleLoginBadCredentials(LoginBadCredentialsException ex) {
        log.warn("密码错误: {}", ex.getMessage());
        return ResponseEntity.failure(401, ex.getMessage());
    }

    // 登录服务异常：500
    @ExceptionHandler(LoginServiceException.class)
    public ResponseEntity<?> handleLoginService(LoginServiceException ex) {
        log.error("登录服务异常", ex);
        return ResponseEntity.failure(500, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("系统内部异常", ex);
        return ResponseEntity.failure(500, ex.getMessage());
    }
}
