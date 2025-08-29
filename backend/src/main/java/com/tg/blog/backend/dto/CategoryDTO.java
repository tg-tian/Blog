package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
}

