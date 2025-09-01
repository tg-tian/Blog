package com.tg.blog.backend.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private final String DEFAULT_VALUE = "0";
    
    // 设置值
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    // 获取值
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            redisTemplate.opsForValue().set(key, DEFAULT_VALUE);
            return DEFAULT_VALUE;
        }
        return value;
    }
    
    // 删除
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    // 计数递增
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
  
}
