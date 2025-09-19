package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.UserMapper;
import com.tg.blog.backend.entity.User;
import com.tg.blog.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Override
    public boolean login(String userName, String password) {
        try {
            User user = userMapper.selectByUsername(userName);
            if (user == null || !user.getPassword().equals(password)) {
                return false; // 用户不存在
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
