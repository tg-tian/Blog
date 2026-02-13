package com.tg.blog.backend.common.exception;

import lombok.Getter;

/**
 * 统一业务异常
 * 
 * @author TG
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
