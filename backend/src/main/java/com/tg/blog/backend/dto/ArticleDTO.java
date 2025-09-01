package com.tg.blog.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String coverUrl;
    private LocalDateTime publishTime;
    private LocalDateTime updateTime;
    private Integer views;
    private Integer likes;
    private Integer comments;
    private Long categoryId;
    private List<Long> tagIds;
    private CategoryDTO category;
    private List<TagDTO> tags;
    
}
