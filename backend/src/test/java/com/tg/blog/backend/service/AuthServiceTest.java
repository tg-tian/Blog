package com.tg.blog.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    
    @Test
    void testLogin() {
        String username = "tgapk";
        String password = "tg852963";
        
        // 假设用户已存在
        boolean result = authService.login(username, password);
        
        // 断言登录成功
        assert result : "Login should be successful for valid credentials";
    }
    

}
