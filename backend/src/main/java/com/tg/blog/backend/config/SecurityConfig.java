package com.tg.blog.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 配置安全相关的Bean和安全策略
 * 
 * @author TG
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置Argon2密码编码器
     * Argon2是一种安全的密码哈希算法，获得了密码哈希竞赛的冠军
     * 
     * @return Argon2PasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用Argon2id变体，盐长度16字节，哈希长度32字节，内存4096KB，迭代3次，并行度1
        return new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
    }
    
    /**
     * 显式定义安全过滤器链：禁用CSRF、表单登录、HTTP Basic，允许所有请求。
     * 目的：避免Spring Security默认登录页重定向（302），将鉴权交由拦截器处理。
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable());
        return http.build();
    }
    
}