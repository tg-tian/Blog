package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.CategoryDTO;
import java.util.List;

/**
 * 分类服务接口
 * 提供文章分类相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface CategoryService {
    
    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     * @return 分类信息
     */
    CategoryDTO getById(Long id);
    
    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    List<CategoryDTO> getAll();
    
    /**
     * 添加新分类
     * @param categoryDTO 分类信息
     */
    void add(CategoryDTO categoryDTO);
    
    /**
     * 更新分类信息
     * @param categoryDTO 分类信息
     */
    void update(CategoryDTO categoryDTO);
    
    /**
     * 删除分类
     * @param id 分类ID
     */
    void delete(Long id);
}
