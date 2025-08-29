package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.service.CategoryService;
import com.tg.blog.backend.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.success(categoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.success(categoryService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody CategoryDTO categoryDTO) {
        categoryService.add(categoryDTO);
        return ResponseEntity.success("Category added successfully");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return ResponseEntity.success("Category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.success("Category deleted successfully");
    }
}
