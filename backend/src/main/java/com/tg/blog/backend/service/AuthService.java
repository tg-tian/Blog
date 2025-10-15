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
     * 用户登录验证并返回JWT
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回JWT字符串，失败返回null
     */
    String login(String username, String password);
    
    /**
     * 创建新用户
     * @param userName 用户名
     * @param password 密码
     * @return 创建是否成功
     */
    boolean createUser(String userName, String password);
    
}
