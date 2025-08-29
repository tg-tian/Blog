package com.tg.blog.backend.dao;

import com.tg.blog.backend.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TagMapper {
    int insert(com.tg.blog.backend.entity.Tag tag);
    int update(com.tg.blog.backend.entity.Tag tag);
    int deleteById(Long id);
    TagDTO selectById(Long id);
    List<TagDTO> selectAll();
}
