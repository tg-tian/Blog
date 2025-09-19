package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 标签统计数据传输对象
 * 用于传输标签及其关联内容数量统计信息
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class TagStatsDTO {
    
    private Long tagId;
    
    private String tagName;
    
    private String tagSlug;
    
    private String tagDescription;
    
    private String tagColor;
    
    private Long articleCount;
}