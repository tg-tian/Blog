package com.tg.blog.backend.dao;

import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目数据访问接口
 * 提供项目相关的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface ProjectMapper {
    
    int insertProject(Project project);
    
    Project selectProjectById(Long id);
    
    List<Project> selectAllProjects();
    
    List<Project> selectProjectsByOrderNum();
    
    int updateProject(Project project);
    
    int deleteProject(Long id);
    
    Long countTotalProjects();
    
    List<TagStatsDTO> selectProjectTagStats();
    
    List<Project> selectProjectsByTag(Long tagId);
}