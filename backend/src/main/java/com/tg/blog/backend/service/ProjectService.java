package com.tg.blog.backend.service;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.dto.TagStatsDTO;

import java.util.List;

/**
 * 项目服务接口
 * 提供项目相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface ProjectService {
    
    /**
     * 创建新项目
     * @param projectDTO 项目数据传输对象
     * @return 创建成功的项目信息
     */
    ProjectDTO createProject(ProjectDTO projectDTO);
    
    /**
     * 根据ID获取项目详情
     * @param id 项目ID
     * @return 项目详情信息
     */
    ProjectDTO getProjectById(Long id);
    
    /**
     * 获取所有项目列表
     * @return 项目列表
     */
    List<ProjectDTO> getAllProjects();
    
    /**
     * 按排序序号获取项目列表
     * @return 按排序序号排列的项目列表
     */
    List<ProjectDTO> getProjectsByOrder();
    
    /**
     * 分页获取项目列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页项目信息
     */
    PageInfo<ProjectDTO> getProjectsByPage(int page, int size);
    
    /**
     * 更新项目信息
     * @param id 项目ID
     * @param projectDTO 更新的项目数据
     * @return 更新后的项目信息
     */
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    
    /**
     * 删除项目
     * @param id 项目ID
     * @return 删除是否成功
     */
    boolean deleteProject(Long id);
    
    /**
     * 获取项目总数
     * @return 项目总数量
     */
    Long getTotalProjectCount();
    
    /**
     * 获取项目标签统计信息
     * @return 标签及其项目数量统计列表
     */
    List<TagStatsDTO> getProjectTagStats();
    
    PageInfo<ProjectDTO> getProjectsByTag(Long tagId, int page, int size);
}