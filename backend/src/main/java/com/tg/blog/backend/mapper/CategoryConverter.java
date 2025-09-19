package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.CategoryDTO;
import com.tg.blog.backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 分类对象转换器
 * 使用MapStruct实现Entity和DTO之间的转换
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CategoryConverter {
    CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);
    
    Category toEntity(CategoryDTO dto);
    
    CategoryDTO toDTO(Category entity);
    
    List<CategoryDTO> toDTOList(List<Category> categories);
}
