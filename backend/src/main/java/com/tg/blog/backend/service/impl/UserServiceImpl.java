package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.UserMapper;
import com.tg.blog.backend.dto.UserDTO;
import com.tg.blog.backend.entity.User;
import com.tg.blog.backend.service.UserService;
import com.tg.blog.backend.common.constants.UserConstants;
import com.tg.blog.backend.util.JwtUtil;
import com.tg.blog.backend.service.cache.RedisService;
import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.common.exception.BusinessException;
import com.tg.blog.backend.service.EmailService;
import io.jsonwebtoken.Claims;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.security.SecureRandom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 * 
 * @author TG
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final long REGISTER_CODE_TTL_SECONDS = 300;
    private static final long REGISTER_SEND_INTERVAL_SECONDS = 60;
    private static final int REGISTER_VERIFY_MAX_ATTEMPTS = 5;
    private static final long REGISTER_TOKEN_TTL_SECONDS = 600;

    private final SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    UserMapper userMapper;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    RedisService redisService;

    @Autowired
    EmailService emailService;
    
    @Override
    public String login(String email, String password) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("邮箱不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        if (user.getStatus() != null && user.getStatus() != UserConstants.Status.ACTIVE.getCode()) {
            throw new BusinessException("用户状态异常，禁止登录");
        }
        String token = jwtUtil.generateToken(user);
        Claims claims = jwtUtil.parseClaims(token);
        long ttlMs = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (ttlMs > 0) {
            String key = RedisKeys.AUTH_USER_TOKEN_PREFIX + user.getUserName();
            redisService.set(key, token, ttlMs, TimeUnit.MILLISECONDS);
        }
        return token;
    }
    
    @Override
    public void sendRegisterCode(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("邮箱不能为空");
        }
        User existingEmailUser = userMapper.selectByEmail(email);
        if (existingEmailUser != null) {
            throw new BusinessException("邮箱已被注册");
        }
        String sendLockKey = RedisKeys.REGISTER_EMAIL_SEND_LOCK_PREFIX + email;
        boolean locked = redisService.setIfAbsent(sendLockKey, "1", REGISTER_SEND_INTERVAL_SECONDS, TimeUnit.SECONDS);
        if (!locked) {
            throw new BusinessException("发送频率过高，请稍后再试");
        }
        
        String code = String.format("%06d", secureRandom.nextInt(1000000));
        String codeKey = RedisKeys.REGISTER_EMAIL_CODE_PREFIX + email;
        String countKey = RedisKeys.REGISTER_EMAIL_VERIFY_COUNT_PREFIX + email;
        
        redisService.set(codeKey, code, REGISTER_CODE_TTL_SECONDS, TimeUnit.SECONDS);
        redisService.set(countKey, "0", REGISTER_CODE_TTL_SECONDS, TimeUnit.SECONDS);
        
        // 异步发送邮件
        emailService.sendRegisterCode(email, code);
        
        log.info("register email code generated: email={}, code={}", email, code);
    }

    @Override
    public String verifyRegisterCode(String email, String code) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("邮箱不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new BusinessException("验证码不能为空");
        }
        String codeKey = RedisKeys.REGISTER_EMAIL_CODE_PREFIX + email;
        String countKey = RedisKeys.REGISTER_EMAIL_VERIFY_COUNT_PREFIX + email;
        String storedCode = redisService.getIfPresent(codeKey);
        if (storedCode == null)
            throw new BusinessException("验证码已过期");
        if (redisService.getIfPresent(countKey) == null)
            redisService.set(countKey, "0", REGISTER_CODE_TTL_SECONDS, TimeUnit.SECONDS);
        Long attempts = redisService.increment(countKey, 1);
        if (attempts != null && attempts >= REGISTER_VERIFY_MAX_ATTEMPTS) {
            redisService.delete(codeKey);
            redisService.delete(countKey);
            throw new BusinessException("验证码错误次数过多");
        }
        if (!storedCode.equals(code))
            throw new BusinessException("验证码错误");
        redisService.delete(codeKey);
        redisService.delete(countKey);
        String registerToken = jwtUtil.generateRegisterToken(email, REGISTER_TOKEN_TTL_SECONDS * 1000);
        redisService.set(RedisKeys.REGISTER_TOKEN_PREFIX + registerToken, email, REGISTER_TOKEN_TTL_SECONDS, TimeUnit.SECONDS);
        return registerToken;
    }

    @Override
    public void completeRegister(String userName, String password, String email, String registerToken) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("邮箱不能为空");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (registerToken == null || registerToken.trim().isEmpty()) {
            throw new BusinessException("邮箱验证失败");
        }
        Claims claims;
        try {
            claims = jwtUtil.parseClaims(registerToken);
        } catch (Exception e) {
            throw new BusinessException("邮箱验证失败");
        }
        String tokenType = claims.get("type", String.class);
        String tokenEmail = claims.get("email", String.class);
        if (!"register".equals(tokenType) || tokenEmail == null || !tokenEmail.equals(email)) {
            throw new BusinessException("邮箱验证失败");
        }
        String storedEmail = redisService.getIfPresent(RedisKeys.REGISTER_TOKEN_PREFIX + registerToken);
        if (storedEmail == null || !storedEmail.equals(email)) {
            throw new BusinessException("邮箱验证失败");
        }
        redisService.delete(RedisKeys.REGISTER_TOKEN_PREFIX + registerToken);
        if (userMapper.selectByEmail(email) != null)
            throw new BusinessException("邮箱已被注册");
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole(UserConstants.Role.USER);
        newUser.setStatus(UserConstants.Status.ACTIVE.getCode());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        userMapper.insertUser(newUser);
    }
}
