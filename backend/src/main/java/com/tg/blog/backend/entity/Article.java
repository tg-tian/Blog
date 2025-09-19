package com.tg.blog.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 * 对应数据库中的article表
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class Article {
    
    /** 文章ID，主键 */
    private Long id;
    
    /** 文章标题 */
    private String title;
    
    /** 文章摘要 */
    private String summary;
    
    /** 封面图片URL */
    private String coverUrl;
    
    /** 文章内容 */
    private String content;
    
    /** 发布时间 */
    private LocalDateTime publishTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /** 浏览量 */
    private Long views;
    
    /** 点赞数 */
    private Long likes;
    
    /** 评论数 */
    private Long comments;

    /** 分类ID，外键关联category表 */
    private Long categoryId;

    /** 删除标记，0-未删除，1-已删除 */
    private Integer deleted;

}
