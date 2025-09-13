package com.tg.blog.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectTagMapper {
    
    int insertProjectTag(@Param("projectId") Long projectId, @Param("tagId") Long tagId);
    
    int batchInsertProjectTags(@Param("projectId") Long projectId, @Param("tagIds") List<Long> tagIds);
    
    List<Long> selectTagIdsByProjectId(Long projectId);
    
    List<Long> selectProjectIdsByTagId(Long tagId);
    
    int deleteByProjectId(Long projectId);
    
    int deleteByTagId(Long tagId);
    
    int deleteProjectTag(@Param("projectId") Long projectId, @Param("tagId") Long tagId);
}