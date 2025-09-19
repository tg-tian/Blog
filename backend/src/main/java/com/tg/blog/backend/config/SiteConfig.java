package com.tg.blog.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 网站配置类
 * 读取application.yaml中site前缀的配置项
 * 
 * @author TG
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "site")
@Data
public class SiteConfig {
    
    /** 网站建站时间，格式：yyyy-MM-ddTHH:mm:ss */
    private String creationDate;
    
    /**
     * 获取建站时间的时间戳（毫秒）
     * @return 建站时间的时间戳
     */
    public long getCreationTimestamp() {
        if (creationDate == null || creationDate.isEmpty()) {
            // 如果没有配置建站时间，返回当前时间
            return System.currentTimeMillis();
        }
        
        try {
            LocalDateTime dateTime = LocalDateTime.parse(creationDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (Exception e) {
            // 解析失败，返回当前时间
            return System.currentTimeMillis();
        }
    }
}