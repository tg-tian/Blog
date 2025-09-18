package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class TagStatsDTO {
    
    private Long tagId;
    
    private String tagName;
    
    private String tagSlug;
    
    private String tagDescription;
    
    private String tagColor;
    
    private Long articleCount;
}