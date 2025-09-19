package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 分类数据访问接口
 * 提供分类相关的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper {
    int insert(Category category);
    
    int update(Category category);
    
    int deleteById(Long id);
    
    Category selectById(Long id);
    
    List<Category> selectAll();
}
