package com.tg.blog.backend.common.exception;

/**
 * 登录失败：服务端异常（数据库、JWT、Redis等内部错误）
 */
public class LoginServiceException extends RuntimeException {
    public LoginServiceException(String message) {
        super(message);
    }

    public LoginServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}