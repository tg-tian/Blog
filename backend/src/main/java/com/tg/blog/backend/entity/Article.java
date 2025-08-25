package com.tg.blog.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    
    private Long id;
    
    private String title;
    
    private String summary;
    
    private String coverUrl;
    
    private String content;
    
    private LocalDateTime publishTime;
    
    private LocalDateTime updateTime;
    
    private Long views;
    
    private Long likes;
    
    private Long comments;

    private Integer deleted;
}
