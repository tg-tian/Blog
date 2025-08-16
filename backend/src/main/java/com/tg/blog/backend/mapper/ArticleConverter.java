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

    Article toEntity(ArticleDTO dto);

    ArticleDTO toDTO(Article entity);

    List<ArticleDTO> toDTOList(List<Article> articles);
}
