package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口
 * 提供用户相关的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户信息，如果不存在则返回null
     */
    User selectByUsername(String userName);
    
    /**
     * 插入新用户
     * @param user 用户信息
     * @return 插入的记录数
     */
    int insertUser(User user);
}
