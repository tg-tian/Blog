package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.UserDTO;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface UserService {


    String login(String email, String password);

    void sendRegisterCode(String email);

    String verifyRegisterCode(String email, String code);

    void completeRegister(String userName, String password, String email, String registerToken);
    
}
