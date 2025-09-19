package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.service.CategoryService;
import com.tg.blog.backend.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类控制器
 * 提供文章分类相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     * @return 分类详情信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.success(categoryService.getById(id));
    }

    /**
     * 获取所有分类列表
     * @return 所有分类的列表
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.success(categoryService.getAll());
    }

    /**
     * 添加新分类
     * @param categoryDTO 分类数据传输对象，包含分类的所有信息
     * @return 添加操作结果
     */
    @PostMapping
    public ResponseEntity<String> add(@RequestBody CategoryDTO categoryDTO) {
        categoryService.add(categoryDTO);
        return ResponseEntity.success("Category added successfully");
    }
    
    /**
     * 更新分类信息
     * @param id 分类ID
     * @param categoryDTO 更新的分类数据
     * @return 更新操作结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        categoryService.update(categoryDTO);
        return ResponseEntity.success("Category updated successfully");
    }

    /**
     * 删除分类
     * @param id 分类ID
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.success("Category deleted successfully");
    }
}
