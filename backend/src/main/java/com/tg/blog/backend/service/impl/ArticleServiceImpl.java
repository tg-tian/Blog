package com.tg.blog.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.exception.ArticleException;
import com.tg.blog.backend.dao.ArticleMapper;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.entity.Article;
import com.tg.blog.backend.mapper.ArticleConverter;
import com.tg.blog.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleConverter articleConverter;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = articleConverter.toEntity(articleDTO);
        article.setViews(0L);
        article.setLikes(0L);
        article.setComments(0L);
        article.setPublishTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        int rows = articleMapper.insertArticle(article);
        if (rows != 1) {
            throw new ArticleException("Failed to insert article into database");
        }
        return articleConverter.toDTO(article);
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleMapper.selectArticleById(id);
        if (article == null) {
            throw new ArticleException("Article not found with id: " + id);
        }
        return articleConverter.toDTO(article);
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleMapper.selectAllArticles();
        if (articles.isEmpty()) {
            throw new ArticleException("No articles found");
        }
        return articleConverter.toDTOList(articles);
    }

    @Override
    public PageInfo<ArticleDTO> getArticlesByPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectAllArticles();
        List<ArticleDTO> articleDTOs = articleConverter.toDTOList(articles);
        return new PageInfo<>(articleDTOs);
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        Article existingArticle = articleMapper.selectArticleById(id);
        if (existingArticle == null) {
            throw new ArticleException("Cannot update. Article not found with id: " + id);
        }
        Article article = articleConverter.toEntity(articleDTO);
        article.setId(id);  // 保证更新的是这条记录
        article.setUpdateTime(LocalDateTime.now());
        int rows = articleMapper.updateArticle(article);
        if (rows != 1) {
            throw new ArticleException("Failed to update article with id: " + id);
        }
        Article updatedArticle = articleMapper.selectArticleById(id);
        return articleConverter.toDTO(updatedArticle);
    }

    @Override
    public boolean deleteArticle(Long id) {
        int rows = articleMapper.deleteArticle(id);
        if (rows != 1) {
            throw new ArticleException("Failed to delete article with id: " + id);
        }
        return true;
    }
}
