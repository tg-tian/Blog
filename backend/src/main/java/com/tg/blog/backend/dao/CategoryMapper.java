package com.tg.blog.backend.dao;

import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    int insert(Category category);
    
    int update(Category category);
    
    int deleteById(Long id);
    
    Category selectById(Long id);
    
    List<Category> selectAll();
}
