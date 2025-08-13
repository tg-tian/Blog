package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByUsername(String userName);
}
