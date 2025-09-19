package com.tg.blog.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目实体类
 * 对应数据库中的project表
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class Project {
    
    /** 项目ID，主键 */
    private Long id;
    
    /** 项目标题 */
    private String title;
    
    /** 项目描述 */
    private String description;
    
    /** 项目详细内容 */
    private String content;
    
    /** 项目封面图片 */
    private String coverImage;
    
    /** 项目链接地址 */
    private String link;
    
    /** 排序序号，用于控制显示顺序 */
    private Integer orderNum;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}