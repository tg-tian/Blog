package com.tg.blog.backend.service;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;

import java.util.List;

public interface ArticleService {

    ArticleDTO createArticle(ArticleDTO articleDTO);

    ArticleDTO getArticleById(Long id);

    List<ArticleDTO> getAllArticles();

    ArticleDTO updateArticle(Long id, ArticleDTO articleDTO);

    boolean deleteArticle(Long id);

    PageInfo<ArticleDTO> getArticlesByPage(int page, int size);

    void likeArticle(Long id);

    void commentArticle(Long id);

    void viewArticle(Long id);
    
    void updateCommentCount(Long id, Integer count);
    
    List<CategoryStatsDTO> getCategoryStats();
    
    PageInfo<ArticleDTO> getArticlesByCategory(Long categoryId, int page, int size);
    
    PageInfo<ArticleDTO> getArticlesByTag(Long tagId, int page, int size);
    
    List<TagStatsDTO> getTagStats();
}
