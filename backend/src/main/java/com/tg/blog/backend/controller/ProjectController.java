package com.tg.blog.backend.controller;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    /**
     * 创建项目
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO project = projectService.createProject(projectDTO);
        return ResponseEntity.success(project);
    }
    
    /**
     * 根据ID获取项目
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.success(project);
    }
    
    /**
     * 获取所有项目列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.success(projects);
    }
    
    /**
     * 按排序获取项目列表
     */
    @GetMapping("/ordered")
    public ResponseEntity<List<ProjectDTO>> getProjectsByOrder() {
        List<ProjectDTO> projects = projectService.getProjectsByOrder();
        return ResponseEntity.success(projects);
    }
    
    /**
     * 分页获取项目列表
     */
    @GetMapping
    public ResponseEntity<PageInfo<ProjectDTO>> getProjectsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<ProjectDTO> projects = projectService.getProjectsByPage(page, size);
        return ResponseEntity.success(projects);
    }
    
    /**
     * 更新项目
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
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.success("Project deleted successfully");
    }
    
    /**
     * 获取项目总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProjectCount() {
        Long count = projectService.getTotalProjectCount();
        return ResponseEntity.success(count);
    }
}