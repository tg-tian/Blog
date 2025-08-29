package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    CategoryDTO getById(Long id);
    List<CategoryDTO> getAll();
    void add(CategoryDTO categoryDTO);
    void update(CategoryDTO categoryDTO);
    void delete(Long id);
}
