package com.tg.blog.backend.common.exception;

/**
 * 文章业务异常类
 * 用于处理文章相关的业务异常
 * 
 * @author TG
 * @since 1.0.0
 */
public class ArticleException extends RuntimeException {
    
    /**
     * 构造方法
     * @param message 异常消息
     */
    public ArticleException(String message) {
        super(message);
    }
    
    /**
     * 构造方法
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ArticleException(String message, Throwable cause) {
        super(message, cause);
    }
}
