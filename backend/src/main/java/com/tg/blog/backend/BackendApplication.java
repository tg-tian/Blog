package com.tg.blog.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 博客系统后端应用启动类
 * Spring Boot应用程序的入口点
 * 
 * @author TG
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.tg.blog.backend.dao")
public class BackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    
}
