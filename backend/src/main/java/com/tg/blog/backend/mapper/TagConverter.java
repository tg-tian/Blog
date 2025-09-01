package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagConverter {
    TagConverter INSTANCE = Mappers.getMapper(TagConverter.class);

    Tag toEntity(TagDTO dto);

    TagDTO toDTO(Tag entity);

    List<TagDTO> toDTOList(List<Tag> tags);
}

