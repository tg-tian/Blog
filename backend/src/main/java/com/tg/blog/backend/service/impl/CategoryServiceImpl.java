package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.CategoryMapper;
import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.entity.Category;
import com.tg.blog.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryDTO getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public void add(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        categoryMapper.insert(category);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        categoryMapper.update(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
