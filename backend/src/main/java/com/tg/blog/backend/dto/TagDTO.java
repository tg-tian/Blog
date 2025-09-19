package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 标签数据传输对象
 * 用于前后端数据传输和API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class TagDTO {
    
    /** 标签ID */
    private Long id;
    
    /** 标签名称 */
    private String name;
    
    /** 标签标识符 */
    private String slug;
    
    /** 标签描述 */
    private String description;
    
    /** 标签颜色 */
    private String color;
}
