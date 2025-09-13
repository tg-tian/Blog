package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {
    
    int insertProject(Project project);
    
    Project selectProjectById(Long id);
    
    List<Project> selectAllProjects();
    
    List<Project> selectProjectsByOrderNum();
    
    int updateProject(Project project);
    
    int deleteProject(Long id);
    
    Long countTotalProjects();
}