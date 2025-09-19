package com.tg.blog.backend.entity;

import lombok.Data;

/**
 * 项目标签关联实体类
 * 对应数据库中的project_tag表，用于维护项目和标签的多对多关系
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class ProjectTag {
    
    /** 项目ID，外键关联project表 */
    private Long projectId;
    
    /** 标签ID，外键关联tag表 */
    private Long tagId;
}