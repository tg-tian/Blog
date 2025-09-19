package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 分类统计数据传输对象
 * 用于传输分类及其文章数量统计信息
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class CategoryStatsDTO {
    
    private Long categoryId;
    
    private String categoryName;
    
    private String categorySlug;
    
    private Long articleCount;
}