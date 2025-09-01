package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    int insert(com.tg.blog.backend.entity.Tag tag);
    
    int update(com.tg.blog.backend.entity.Tag tag);
    
    int deleteById(Long id);
    
    Tag selectById(Long id);
    
    List<Tag> selectAll();
    
    List<Tag> selectByIds(List<Long> ids);
}
