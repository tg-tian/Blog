package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.CategoryMapper;
import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.entity.Category;
import com.tg.blog.backend.mapper.CategoryConverter;
import com.tg.blog.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 分类服务实现类
 * 实现分类相关的业务逻辑
 * 
 * @author TG
 * @since 1.0.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public CategoryDTO getById(Long id) {
        Category category = categoryMapper.selectById(id);
        return categoryConverter.toDTO(category);
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryMapper.selectAll();
        return categoryConverter.toDTOList(categories);
    }

    @Override
    public void add(CategoryDTO categoryDTO) {
        Category category = categoryConverter.toEntity(categoryDTO);
        categoryMapper.insert(category);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = categoryConverter.toEntity(categoryDTO);
        categoryMapper.update(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
