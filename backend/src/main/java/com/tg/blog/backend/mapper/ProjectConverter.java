package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.ProjectDTO;
import com.tg.blog.backend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectConverter {
    
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);
    
    @Mapping(target = "orderNum", defaultValue = "0")
    @Mapping(target = "createTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    Project toEntity(ProjectDTO dto);
    
    ProjectDTO toDTO(Project entity);
    
    List<ProjectDTO> toDTOList(List<Project> projects);
    
    List<Project> toEntityList(List<ProjectDTO> dtos);
}