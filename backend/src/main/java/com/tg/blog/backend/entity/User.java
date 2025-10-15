package com.tg.blog.backend.entity;

import lombok.Data;

/**
 * 用户实体类
 * 对应数据库中的user表
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class User {
    
    /** 用户ID，主键 */
    private Long id;
    
    /** 用户名，用于登录 */
    private String userName;
    
    /** 用户密码 */
    private String password;

    /** 用户角色，如 USER/ADMIN */
    private String role;
}
