package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleConverter {

    ArticleConverter INSTANCE = Mappers.getMapper(ArticleConverter.class);
    
    @Mapping(target = "views", defaultValue = "0L")
    @Mapping(target = "likes", defaultValue = "0L")
    @Mapping(target = "comments", defaultValue = "0L")
    @Mapping(target = "publishTime", source = "publishTime",
            defaultExpression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    Article toEntity(ArticleDTO dto);

    ArticleDTO toDTO(Article entity);

    List<ArticleDTO> toDTOList(List<Article> articles);
}
