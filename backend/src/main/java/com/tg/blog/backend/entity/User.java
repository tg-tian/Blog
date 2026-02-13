package com.tg.blog.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

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

    /** 邮箱 */
    private String email;

    /** 用户状态：1-正常 2-冻结 3-封禁 */
    private Integer status;

    /** 用户头像URL */
    private String avatar;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
