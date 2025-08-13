package com.tg.blog.backend.controller;

import com.tg.blog.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {
    
    @Autowired
    AuthService authService;
    
    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password) {
        if (authService.login(username, password)) {
            return "success";
        }
        return "fail";
    }
}
