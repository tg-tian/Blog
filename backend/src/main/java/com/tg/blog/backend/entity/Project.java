package com.tg.blog.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Project {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private String content;
    
    private String coverImage;
    
    private String link;
    
    private Integer orderNum;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}