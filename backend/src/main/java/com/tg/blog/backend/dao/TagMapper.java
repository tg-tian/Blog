package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 标签数据访问接口
 * 提供标签相关的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface TagMapper {
    int insert(com.tg.blog.backend.entity.Tag tag);
    
    int update(com.tg.blog.backend.entity.Tag tag);
    
    int deleteById(Long id);
    
    Tag selectById(Long id);
    
    List<Tag> selectAll();
    
    List<Tag> selectByIds(List<Long> ids);
}
