package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class CategoryStatsDTO {
    
    private Long categoryId;
    
    private String categoryName;
    
    private String categorySlug;
    
    private Long articleCount;
}