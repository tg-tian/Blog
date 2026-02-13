package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.service.EmailService;
import com.tg.blog.backend.service.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 邮件服务实现类
 * 
 * @author TG
 * @since 1.0.0
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisService redisService;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Async
    @Override
    public void sendRegisterCode(String to, String code) {
        if (mailFrom == null || mailFrom.trim().isEmpty()) {
            log.error("邮件发送失败：未配置发送者邮箱");
            rollbackRegisterCode(to);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(mailFrom);
        message.setSubject("春风沂水注册验证码");
        message.setText("您的注册验证码为：" + code + "，5分钟内有效。");

        try {
            mailSender.send(message);
            log.info("验证码邮件已异步发送至: {}", to);
        } catch (Exception ex) {
            log.error("验证码邮件发送失败: {}", ex.getMessage(), ex);
            rollbackRegisterCode(to);
        }
    }

    private void rollbackRegisterCode(String email) {
        String codeKey = RedisKeys.REGISTER_EMAIL_CODE_PREFIX + email;
        String countKey = RedisKeys.REGISTER_EMAIL_VERIFY_COUNT_PREFIX + email;
        String sendLockKey = RedisKeys.REGISTER_EMAIL_SEND_LOCK_PREFIX + email;
        
        redisService.delete(codeKey);
        redisService.delete(countKey);
        redisService.delete(sendLockKey);
        log.info("已回滚用户 {} 的注册验证码缓存", email);
    }
}
