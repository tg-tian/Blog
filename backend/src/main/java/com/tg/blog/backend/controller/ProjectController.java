package com.tg.blog.backend.controller;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目控制器
 * 提供项目相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    /**
     * 创建新项目
     * @param projectDTO 项目数据传输对象，包含项目的所有信息
     * @return 创建成功的项目信息
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO project = projectService.createProject(projectDTO);
        return ResponseEntity.success(project);
    }
    
    /**
     * 根据ID获取项目详情
     * @param id 项目ID
     * @return 项目详情信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.success(project);
    }
    
    /**
     * 获取所有项目列表
     * @return 所有项目的列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.success(projects);
    }
    
    /**
     * 按排序序号获取项目列表
     * @return 按排序序号排列的项目列表
     */
    @GetMapping("/ordered")
    public ResponseEntity<List<ProjectDTO>> getProjectsByOrder() {
        List<ProjectDTO> projects = projectService.getProjectsByOrder();
        return ResponseEntity.success(projects);
    }
    
    /**
     * 分页获取项目列表
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @return 分页后的项目列表
     */
    @GetMapping
    public ResponseEntity<PageInfo<ProjectDTO>> getProjectsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<ProjectDTO> projects = projectService.getProjectsByPage(page, size);
        return ResponseEntity.success(projects);
    }
    
    /**
     * 更新项目信息
     * @param id 项目ID
     * @param projectDTO 更新的项目数据
     * @return 更新后的项目信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectDTO) {
        ProjectDTO project = projectService.updateProject(id, projectDTO);
        return ResponseEntity.success(project);
    }
    
    /**
     * 删除项目
     * @param id 项目ID
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.success("Project deleted successfully");
    }
    
    /**
     * 获取项目总数
     * @return 项目总数量
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProjectCount() {
        Long count = projectService.getTotalProjectCount();
        return ResponseEntity.success(count);
    }
    
    /**
     * 获取所有标签及其项目数量统计
     * @return 标签统计信息列表，包含标签名称和对应的项目数量
     */
    @GetMapping("/tags/stats")
    public ResponseEntity<List<TagStatsDTO>> getProjectTagStats() {
        List<TagStatsDTO> tagStats = projectService.getProjectTagStats();
        return ResponseEntity.success(tagStats);
    }
    
    /**
     * 根据标签ID分页获取项目列表
     * @param tagId 标签ID
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @return 指定标签下的分页项目列表
     */
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<PageInfo<ProjectDTO>> getProjectsByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<ProjectDTO> projects = projectService.getProjectsByTag(tagId, page, size);
        return ResponseEntity.success(projects);
    }
}