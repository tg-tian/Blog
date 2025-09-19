package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.UserDTO;
import com.tg.blog.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 提供用户认证相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthService authService;
    
    /**
     * 用户登录
     * @param userDTO 用户登录信息，包含用户名和密码
     * @return 登录结果，成功返回"success"，失败返回"fail"
     */
    @PostMapping("/login")
    public String Login(@RequestBody UserDTO userDTO) {
        if (authService.login(userDTO.getUsername(), userDTO.getPassword())) {
            return "success";
        }
        return "fail";
    }
}
