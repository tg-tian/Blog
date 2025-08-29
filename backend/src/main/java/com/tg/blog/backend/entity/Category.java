package com.tg.blog.backend.entity;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
}
