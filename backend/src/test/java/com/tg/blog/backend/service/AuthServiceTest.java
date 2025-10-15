package com.tg.blog.backend.service;

import com.tg.blog.backend.dao.UserMapper;
import com.tg.blog.backend.entity.User;
import com.tg.blog.backend.constants.RoleConstants;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Claims;
import com.tg.blog.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Test
    void testLogin() {
        String username = "loginuser_" + System.currentTimeMillis();
        String password = "loginPass123";

        // 先创建用户，确保存在且密码与角色正确
        boolean created = authService.createUser(username, password);
        assert created : "User creation should be successful for login test";

        // 登录返回JWT token
        String token = authService.login(username, password);
        assert token != null && !token.isEmpty() : "Token should be returned for valid credentials";

        // 校验并解析token的claims
        assert jwtUtil.validateToken(token) : "Token should be valid";
        Claims claims = jwtUtil.parseClaims(token);
        assert claims.getSubject().equals(username) : "JWT subject should be username";
        assert RoleConstants.USER.equals(claims.get("role", String.class)) : "Role claim should be USER";
    }
    
    @Test
    @Transactional
    @Commit
    void testCreateUserSuccess() {
        String username = "testuser1" + System.currentTimeMillis(); // 使用时间戳确保用户名唯一
        String password = "testpassword123";
        
        // 测试创建用户成功
        boolean result = authService.createUser(username, password);
        
        // 断言创建成功
        assert result : "User creation should be successful";
        // 验证用户确实被创建到数据库中
        User createdUser = userMapper.selectByUsername(username);
        assert createdUser != null : "Created user should exist in database";
        assert createdUser.getUserName().equals(username) : "Username should match";
        // 验证默认角色
        assert RoleConstants.USER.equals(createdUser.getRole()) : "Default role should be USER";

        // 验证密码被正确加密
        assert passwordEncoder.matches(password, createdUser.getPassword()) : "Password should be correctly encrypted";
    }

}
