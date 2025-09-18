package com.tg.blog.backend.service;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.dto.TagStatsDTO;

import java.util.List;

public interface ProjectService {
    
    ProjectDTO createProject(ProjectDTO projectDTO);
    
    ProjectDTO getProjectById(Long id);
    
    List<ProjectDTO> getAllProjects();
    
    List<ProjectDTO> getProjectsByOrder();
    
    PageInfo<ProjectDTO> getProjectsByPage(int page, int size);
    
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    
    boolean deleteProject(Long id);
    
    Long getTotalProjectCount();
    
    List<TagStatsDTO> getProjectTagStats();
    
    PageInfo<ProjectDTO> getProjectsByTag(Long tagId, int page, int size);
}