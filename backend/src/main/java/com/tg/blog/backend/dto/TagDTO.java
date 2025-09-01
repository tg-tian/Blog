package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class TagDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String color;
}
