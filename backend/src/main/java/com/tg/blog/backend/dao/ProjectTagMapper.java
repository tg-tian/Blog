package com.tg.blog.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目标签关联数据访问接口
 * 提供项目和标签多对多关系的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface ProjectTagMapper {
    
    /**
     * 插入项目标签关联记录
     * @param projectId 项目ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    int insertProjectTag(@Param("projectId") Long projectId, @Param("tagId") Long tagId);
    
    /**
     * 批量插入项目标签关联记录
     * @param projectId 项目ID
     * @param tagIds 标签ID列表
     * @return 影响的行数
     */
    int batchInsertProjectTags(@Param("projectId") Long projectId, @Param("tagIds") List<Long> tagIds);
    
    /**
     * 根据项目ID查询关联的标签ID列表
     * @param projectId 项目ID
     * @return 标签ID列表
     */
    List<Long> selectTagIdsByProjectId(Long projectId);
    
    /**
     * 根据标签ID查询关联的项目ID列表
     * @param tagId 标签ID
     * @return 项目ID列表
     */
    List<Long> selectProjectIdsByTagId(Long tagId);
    
    /**
     * 根据项目ID删除所有关联的标签记录
     * @param projectId 项目ID
     * @return 影响的行数
     */
    int deleteByProjectId(Long projectId);
    
    /**
     * 根据标签ID删除所有关联的项目记录
     * @param tagId 标签ID
     * @return 影响的行数
     */
    int deleteByTagId(Long tagId);
    
    /**
     * 删除指定项目和标签的关联记录
     * @param projectId 项目ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    int deleteProjectTag(@Param("projectId") Long projectId, @Param("tagId") Long tagId);
}