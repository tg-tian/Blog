package com.tg.blog.backend.entity;

import lombok.Data;

/**
 * 标签实体类
 * 对应数据库中的tag表
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class Tag {
    
    /** 标签ID，主键 */
    private Long id;
    
    /** 标签名称 */
    private String name;
    
    /** 标签标识符，用于URL */
    private String slug;
    
    /** 标签描述 */
    private String description;
    
    /** 标签颜色，用于前端显示 */
    private String color;
}
