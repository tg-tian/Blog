package com.tg.blog.backend.service;

/**
 * 邮件服务接口
 * 提供邮件发送相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface EmailService {

    /**
     * 异步发送注册验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     */
    void sendRegisterCode(String to, String code);
}
