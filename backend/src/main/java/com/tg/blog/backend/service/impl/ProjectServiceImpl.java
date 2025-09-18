package com.tg.blog.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.exception.ArticleException;
import com.tg.blog.backend.dao.ProjectMapper;
import com.tg.blog.backend.dao.ProjectTagMapper;
import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Project;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.mapper.ProjectConverter;
import com.tg.blog.backend.mapper.TagConverter;
import com.tg.blog.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    ProjectConverter projectConverter;
    
    @Autowired
    TagConverter tagConverter;
    
    @Autowired
    ProjectMapper projectMapper;
    
    @Autowired
    ProjectTagMapper projectTagMapper;
    
    @Autowired
    TagMapper tagMapper;
    
    @Transactional
    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = projectConverter.toEntity(projectDTO);
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        projectMapper.insertProject(project);
        
        // 处理标签关联
        if (projectDTO.getTagIds() != null && !projectDTO.getTagIds().isEmpty()) {
            projectTagMapper.batchInsertProjectTags(project.getId(), projectDTO.getTagIds());
        }
        
        return projectConverter.toDTO(project);
    }
    
    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectMapper.selectProjectById(id);
        if (project == null) {
            throw new ArticleException("Project not found with id: " + id);
        }
        
        ProjectDTO projectDTO = projectConverter.toDTO(project);
        
        // 获取关联的标签
        List<Long> tagIds = projectTagMapper.selectTagIdsByProjectId(project.getId());
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagMapper.selectByIds(tagIds);
            projectDTO.setTags(tagConverter.toDTOList(tags));
            projectDTO.setTagIds(tagIds);
        }
        
        return projectDTO;
    }
    
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectMapper.selectAllProjects();
        return convertProjectsWithTags(projects);
    }
    
    @Override
    public List<ProjectDTO> getProjectsByOrder() {
        List<Project> projects = projectMapper.selectProjectsByOrderNum();
        return convertProjectsWithTags(projects);
    }
    
    @Override
    public PageInfo<ProjectDTO> getProjectsByPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Project> projects = projectMapper.selectAllProjects();
        List<ProjectDTO> projectDTOs = convertProjectsWithTags(projects);
        return new PageInfo<>(projectDTOs);
    }
    
    @Transactional
    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project existingProject = projectMapper.selectProjectById(id);
        if (existingProject == null) {
            throw new ArticleException("Cannot update. Project not found with id: " + id);
        }
        
        Project project = projectConverter.toEntity(projectDTO);
        project.setId(id);
        project.setUpdateTime(LocalDateTime.now());
        projectMapper.updateProject(project);
        
        // 更新标签关联
        projectTagMapper.deleteByProjectId(project.getId());
        if (projectDTO.getTagIds() != null && !projectDTO.getTagIds().isEmpty()) {
            projectTagMapper.batchInsertProjectTags(project.getId(), projectDTO.getTagIds());
        }
        
        Project updatedProject = projectMapper.selectProjectById(id);
        return projectConverter.toDTO(updatedProject);
    }
    
    @Transactional
    @Override
    public boolean deleteProject(Long id) {
        // 先删除标签关联
        projectTagMapper.deleteByProjectId(id);
        
        // 删除项目
        int rows = projectMapper.deleteProject(id);
        if (rows != 1) {
            throw new ArticleException("Failed to delete project with id: " + id);
        }
        return true;
    }
    
    @Override
    public Long getTotalProjectCount() {
        return projectMapper.countTotalProjects();
    }
    
    @Override
    public List<TagStatsDTO> getProjectTagStats() {
        return projectMapper.selectProjectTagStats();
    }
    
    @Override
    public PageInfo<ProjectDTO> getProjectsByTag(Long tagId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Project> projects = projectMapper.selectProjectsByTag(tagId);
        List<ProjectDTO> result = convertProjectsWithTags(projects);
        return new PageInfo<>(result);
    }
    
    private List<ProjectDTO> convertProjectsWithTags(List<Project> projects) {
        List<ProjectDTO> result = new ArrayList<>();
        for (Project project : projects) {
            ProjectDTO dto = projectConverter.toDTO(project);
            
            // 获取关联的标签
            List<Long> tagIds = projectTagMapper.selectTagIdsByProjectId(project.getId());
            if (tagIds != null && !tagIds.isEmpty()) {
                List<Tag> tags = tagMapper.selectByIds(tagIds);
                dto.setTags(tagConverter.toDTOList(tags));
                dto.setTagIds(tagIds);
            }
            
            result.add(dto);
        }
        return result;
    }
}