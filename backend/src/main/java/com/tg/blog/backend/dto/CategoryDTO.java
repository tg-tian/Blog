package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 分类数据传输对象
 * 用于前后端数据传输和API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class CategoryDTO {
    
    /** 分类ID */
    private Long id;
    
    /** 分类名称 */
    private String name;
    
    /** 分类标识符 */
    private String slug;
    
    /** 分类描述 */
    private String description;
    
    /** 父分类ID */
    private Long parentId;
}

