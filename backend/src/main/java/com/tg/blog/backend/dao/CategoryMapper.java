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

    /**
     * 批量查询分类
     * @param ids 分类ID集合
     * @return 分类列表
     */
    List<Category> selectByIds(List<Long> ids);
    
    List<Category> selectAll();
}
