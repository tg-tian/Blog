package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 标签对象转换器
 * 使用MapStruct实现Entity和DTO之间的转换
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface TagConverter {
    TagConverter INSTANCE = Mappers.getMapper(TagConverter.class);

    Tag toEntity(TagDTO dto);

    TagDTO toDTO(Tag entity);

    List<TagDTO> toDTOList(List<Tag> tags);
}

