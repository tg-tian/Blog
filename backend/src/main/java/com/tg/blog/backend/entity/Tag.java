package com.tg.blog.backend.entity;

import lombok.Data;

@Data
public class Tag {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String color;
}
