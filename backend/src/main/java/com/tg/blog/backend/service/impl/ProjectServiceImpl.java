package com.tg.blog.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.exception.BusinessException;
import com.tg.blog.backend.dao.ProjectMapper;
import com.tg.blog.backend.dao.ProjectTagMapper;
import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Project;
import com.tg.blog.backend.entity.ProjectTag;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.mapper.ProjectConverter;
import com.tg.blog.backend.mapper.TagConverter;
import com.tg.blog.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目服务实现类
 * 实现项目相关的业务逻辑，包括CRUD操作和标签关联管理
 * 
 * @author TG
 * @since 1.0.0
 */
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
            throw new BusinessException("Project not found with id: " + id);
        }
        List<ProjectDTO> result = convertProjectsWithTags(List.of(project));
        return result.get(0);
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
            throw new BusinessException("Cannot update. Project not found with id: " + id);
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
            throw new BusinessException("Failed to delete project with id: " + id);
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
        if (projects == null || projects.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> projectIds = new ArrayList<>();
        for (Project project : projects) {
            if (project.getId() != null) {
                projectIds.add(project.getId());
            }
        }
        Map<Long, List<Long>> tagIdsByProjectId = new HashMap<>();
        if (!projectIds.isEmpty()) {
            List<ProjectTag> relations = projectTagMapper.selectProjectTagsByProjectIds(projectIds);
            for (ProjectTag relation : relations) {
                if (relation == null || relation.getProjectId() == null || relation.getTagId() == null) {
                    continue;
                }
                tagIdsByProjectId
                        .computeIfAbsent(relation.getProjectId(), key -> new ArrayList<>())
                        .add(relation.getTagId());
            }
        }
        Set<Long> tagIds = new HashSet<>();
        for (List<Long> ids : tagIdsByProjectId.values()) {
            if (ids != null) {
                tagIds.addAll(ids);
            }
        }
        Map<Long, Tag> tagMap = new HashMap<>();
        if (!tagIds.isEmpty()) {
            List<Tag> tags = tagMapper.selectByIds(new ArrayList<>(tagIds));
            for (Tag tag : tags) {
                tagMap.put(tag.getId(), tag);
            }
        }
        List<ProjectDTO> result = new ArrayList<>();
        for (Project project : projects) {
            ProjectDTO dto = projectConverter.toDTO(project);
            
            // 获取关联的标签
            List<Long> tagIdList = tagIdsByProjectId.get(project.getId());
            if (tagIdList != null && !tagIdList.isEmpty()) {
                List<Tag> tags = new ArrayList<>();
                for (Long tagId : tagIdList) {
                    Tag tag = tagMap.get(tagId);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                if (!tags.isEmpty()) {
                    dto.setTags(tagConverter.toDTOList(tags));
                    dto.setTagIds(tagIdList);
                }
            }
            
            result.add(dto);
        }
        return result;
    }
}
