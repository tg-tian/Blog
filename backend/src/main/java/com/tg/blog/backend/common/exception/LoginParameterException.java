package com.tg.blog.backend.common.exception;

/**
 * 登录失败：参数不合法（用户名或密码为空等）
 */
public class LoginParameterException extends RuntimeException {
    public LoginParameterException(String message) {
        super(message);
    }

    public LoginParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}