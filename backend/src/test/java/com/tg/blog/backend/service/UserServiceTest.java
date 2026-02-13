package com.tg.blog.backend.service;

import com.tg.blog.backend.dao.UserMapper;
import com.tg.blog.backend.dto.UserDTO;
import com.tg.blog.backend.entity.User;
import com.tg.blog.backend.common.constants.UserConstants;
import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.service.cache.RedisService;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Claims;
import com.tg.blog.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @MockBean
    private JavaMailSender mailSender;
    
    @Test
    void testLogin() {
        String username = "testuser11770824392063" + System.currentTimeMillis();
        String password = "loginPass123";
        String email = "login" + System.currentTimeMillis() + "@example.com";

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);

        userService.sendRegisterCode(email);
        String code = redisService.getIfPresent(RedisKeys.REGISTER_EMAIL_CODE_PREFIX + email);
        String registerToken = userService.verifyRegisterCode(email, code);
        userDTO.setRegisterToken(registerToken);
        userService.completeRegister(username, password, email, registerToken);

        // 登录返回JWT token
        String token = userService.login(username, password);
        assert token != null && !token.isEmpty() : "Token should be returned for valid credentials";

        // 校验并解析token的claims
        assert jwtUtil.validateToken(token) : "Token should be valid";
        Claims claims = jwtUtil.parseClaims(token);
        assert claims.getSubject().equals(username) : "JWT subject should be username";
        assert UserConstants.Role.USER.equals(claims.get("role", String.class)) : "Role claim should be USER";
    }
    
    @Test
    @Transactional
    @Commit
    void testRegisterSuccess() {
        String username = "testuser2" + System.currentTimeMillis(); // 使用时间戳确保用户名唯一
        String password = "testpassword123";
        String email = "test" + System.currentTimeMillis() + "@example.com";
        
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);

        userService.sendRegisterCode(email);
        String code = redisService.getIfPresent(RedisKeys.REGISTER_EMAIL_CODE_PREFIX + email);
        String registerToken = userService.verifyRegisterCode(email, code);
        userDTO.setRegisterToken(registerToken);
        userService.completeRegister(username, password, email, registerToken);

        // 验证用户确实被创建到数据库中
        User createdUser = userMapper.selectByUsername(username);
        assert createdUser != null : "Created user should exist in database";
        assert createdUser.getUserName().equals(username) : "Username should match";
        assert createdUser.getEmail().equals(email) : "Email should match";
        // 验证默认角色
        assert UserConstants.Role.USER.equals(createdUser.getRole()) : "Default role should be USER";

        // 验证密码被正确加密
        assert passwordEncoder.matches(password, createdUser.getPassword()) : "Password should be correctly encrypted";
    }

}
