package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.UserDTO;
import com.tg.blog.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthService authService;
    
    @PostMapping("/login")
    public String Login(@RequestBody UserDTO userDTO) {
        if (authService.login(userDTO.getUsername(), userDTO.getPassword())) {
            return "success";
        }
        return "fail";
    }
}
