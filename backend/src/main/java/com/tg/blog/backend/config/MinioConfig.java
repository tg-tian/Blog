package com.tg.blog.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO对象存储配置类
 * 读取application.yaml中minio前缀的配置项
 * 
 * @author TG
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {
    
    /** MinIO服务器端点地址，如：http://localhost:9000 */
    private String endpoint;
    
    /** MinIO访问密钥ID，用于身份验证 */
    private String accessKey;
    
    /** MinIO访问密钥，用于身份验证 */
    private String secretKey;
    
    /** MinIO存储桶名称，用于存储文件 */
    private String bucket;

}

