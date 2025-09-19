package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 用户数据传输对象
 * 用于前后端数据传输和API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class UserDTO {
    
    /** 用户名 */
    private String username;
    
    /** 用户密码 */
    private String password;
}
