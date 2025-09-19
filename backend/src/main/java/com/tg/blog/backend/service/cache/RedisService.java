package com.tg.blog.backend.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务类
 * 提供Redis基础操作的封装
 * 
 * @author TG
 * @since 1.0.0
 */
@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /** 默认值，当key不存在时返回 */
    private final String DEFAULT_VALUE = "0";
    
    /**
     * 设置键值对，带过期时间
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    /**
     * 获取指定键的值
     * @param key 键
     * @return 值，如果不存在则返回默认值"0"
     */
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            redisTemplate.opsForValue().set(key, DEFAULT_VALUE);
            return DEFAULT_VALUE;
        }
        return value;
    }
    
    /**
     * 删除指定键
     * @param key 要删除的键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 对指定键的值进行递增操作
     * @param key 键
     * @param delta 递增量
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
  
}
