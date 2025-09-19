package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.service.TagService;
import com.tg.blog.backend.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 标签控制器
 * 提供标签相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 根据ID获取标签信息
     * @param id 标签ID
     * @return 标签详情信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable Long id) {
        return ResponseEntity.success(tagService.getById(id));
    }

    /**
     * 获取所有标签列表
     * @return 所有标签的列表
     */
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll() {
        return ResponseEntity.success(tagService.getAll());
    }

    /**
     * 添加新标签
     * @param tagDTO 标签数据传输对象，包含标签的所有信息
     * @return 添加操作结果
     */
    @PostMapping
    public ResponseEntity<String> add(@RequestBody TagDTO tagDTO) {
        tagService.add(tagDTO);
        return ResponseEntity.success("Tag added successfully");
    }
    
    /**
     * 更新标签信息
     * @param id 标签ID
     * @param tagDTO 更新的标签数据
     * @return 更新操作结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        tagDTO.setId(id);
        tagService.update(tagDTO);
        return ResponseEntity.success("Tag updated successfully");
    }

    /**
     * 删除标签
     * @param id 标签ID
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.success("Tag deleted successfully");
    }
}
