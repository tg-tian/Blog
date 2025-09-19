package com.tg.blog.backend.mapper;

import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 文章对象转换器
 * 使用MapStruct实现Entity和DTO之间的转换
 * 
 * @author TG
 * @since 1.0.0
 */
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
