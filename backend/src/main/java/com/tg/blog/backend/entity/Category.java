package com.tg.blog.backend.entity;

import lombok.Data;

/**
 * 分类实体类
 * 对应数据库中的category表
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class Category {
    
    /** 分类ID，主键 */
    private Long id;
    
    /** 分类名称 */
    private String name;
    
    /** 分类标识符，用于URL */
    private String slug;
    
    /** 分类描述 */
    private String description;
    
    /** 父分类ID，用于构建分类层级关系 */
    private Long parentId;
}
