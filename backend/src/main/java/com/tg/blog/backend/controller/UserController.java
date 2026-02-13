package com.tg.blog.backend.controller;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.UserDTO;
import com.tg.blog.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 用户控制器
 * 提供用户相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;
    
    /**
     * 用户登录
     * @param userDTO 用户登录信息，包含用户名和密码
     * @return 登录成功返回JWT token字符串，失败返回"fail"
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO.getEmail(), userDTO.getPassword());
        return  ResponseEntity.success(token != null ? token : "登录失败");
    }

    @PostMapping("/register/code")
    public ResponseEntity<String> sendRegisterCode(@RequestBody UserDTO userDTO) {
        userService.sendRegisterCode(userDTO.getEmail());
        return ResponseEntity.success("验证码已发送");
    }

    @PostMapping("/register/verify")
    public ResponseEntity<String> verifyRegisterCode(@RequestBody UserDTO userDTO) {
        String refreshToken =  userService.verifyRegisterCode(userDTO.getEmail(), userDTO.getCode());
        return ResponseEntity.success(refreshToken);
    }
    
    /**
     * 创建新用户
     * @param userDTO 用户注册信息，包含用户名、密码和邮箱
     * @return 创建结果，成功返回"success"，失败返回"fail"
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        String userName = userDTO.getUsername();
        String password = userDTO.getPassword();
        String email = userDTO.getEmail();
        String registerToken = userDTO.getRegisterToken();
        userService.completeRegister(userName, password, email, registerToken);
        return ResponseEntity.success("注册成功");
    }
}
