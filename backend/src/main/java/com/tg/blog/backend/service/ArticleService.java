package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {

    ArticleDTO createArticle(ArticleDTO articleDTO);

    ArticleDTO getArticleById(Long id);

    List<ArticleDTO> getAllArticles();

    ArticleDTO updateArticle(Long id, ArticleDTO articleDTO);

    boolean deleteArticle(Long id);
}
