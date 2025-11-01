package com.tg.blog.backend.common.exception;

/**
 * 登录失败：用户不存在
 */
public class LoginUserNotFoundException extends RuntimeException {
    public LoginUserNotFoundException(String message) {
        super(message);
    }

    public LoginUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}