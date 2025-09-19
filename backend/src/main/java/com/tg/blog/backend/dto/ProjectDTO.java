package com.tg.blog.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目数据传输对象
 * 用于前后端数据传输和API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class ProjectDTO {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private String content;
    
    private String coverImage;
    
    private String link;
    
    private Integer orderNum;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private List<Long> tagIds;
    
    private List<TagDTO> tags;
}