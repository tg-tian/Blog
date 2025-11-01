package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.UserMapper;
import com.tg.blog.backend.entity.User;
import com.tg.blog.backend.service.AuthService;
import com.tg.blog.backend.constants.RoleConstants;
import com.tg.blog.backend.util.JwtUtil;
import com.tg.blog.backend.service.cache.RedisService;
import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.common.exception.LoginBadCredentialsException;
import com.tg.blog.backend.common.exception.LoginParameterException;
import com.tg.blog.backend.common.exception.LoginServiceException;
import com.tg.blog.backend.common.exception.LoginUserNotFoundException;
import io.jsonwebtoken.Claims;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 * 实现用户认证相关的业务逻辑
 * 
 * @author TG
 * @since 1.0.0
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    UserMapper userMapper;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    RedisService redisService;
    
    @Override
    public String login(String userName, String password) {
        try {
            // 参数校验
            if (userName == null || userName.trim().isEmpty()) {
                throw new LoginParameterException("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new LoginParameterException("密码不能为空");
            }

            User user = userMapper.selectByUsername(userName);
            if (user == null) {
                throw new LoginUserNotFoundException("用户不存在");
            }
            // 使用Argon2PasswordEncoder验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new LoginBadCredentialsException("用户名或密码错误");
            }
            // 生成JWT
            String token = jwtUtil.generateToken(user);
            // 保存token到Redis，TTL与JWT过期时间一致
            Claims claims = jwtUtil.parseClaims(token);
            long ttlMs = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (ttlMs > 0) {
                String key = RedisKeys.AUTH_USER_TOKEN_PREFIX + user.getUserName();
                redisService.set(key, token, ttlMs, TimeUnit.MILLISECONDS);
            }
            // 返回JWT
            return token;
        } catch (LoginParameterException | LoginUserNotFoundException | LoginBadCredentialsException e) {
            // 明确的业务异常直接抛出，由全局异常处理器统一返回
            throw e;
        } catch (Exception e) {
            // 未预期异常包装为服务异常
            throw new LoginServiceException("登录服务异常，请稍后重试", e);
        }
    }
    
    @Override
    public boolean createUser(String userName, String password) {
        try {
            // 检查用户名是否已存在
            User existingUser = userMapper.selectByUsername(userName);
            if (existingUser != null) {
                return false; // 用户名已存在
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUserName(userName);
            // 使用Argon2PasswordEncoder加密密码
            newUser.setPassword(passwordEncoder.encode(password));
            // 设置默认角色
            newUser.setRole(RoleConstants.USER);
            
            int result = userMapper.insertUser(newUser);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("创建用户失败", e);
        }
    }
}
