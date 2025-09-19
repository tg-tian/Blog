package com.tg.blog.backend.service;

/**
 * 认证服务接口
 * 提供用户认证相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    boolean login(String username, String password);
    
}
