package com.tg.blog.backend.common.exception;

/**
 * 登录失败：密码错误
 */
public class LoginBadCredentialsException extends RuntimeException {
    public LoginBadCredentialsException(String message) {
        super(message);
    }

    public LoginBadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}