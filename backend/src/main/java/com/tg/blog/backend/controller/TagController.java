package com.tg.blog.backend.controller;

import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.service.TagService;
import com.tg.blog.backend.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable Long id) {
        return ResponseEntity.success(tagService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll() {
        return ResponseEntity.success(tagService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody TagDTO tagDTO) {
        tagService.add(tagDTO);
        return ResponseEntity.success("Tag added successfully");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        tagDTO.setId(id);
        tagService.update(tagDTO);
        return ResponseEntity.success("Tag updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.success("Tag deleted successfully");
    }
}
