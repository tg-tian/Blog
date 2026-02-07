package com.tg.blog.backend;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * 博客系统后端应用启动类
 * Spring Boot应用程序的入口点
 * 
 * @author TG
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@MapperScan("com.tg.blog.backend.dao")
public class BackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        log.debug("博客系统后端启动成功!");
    }
    
}
