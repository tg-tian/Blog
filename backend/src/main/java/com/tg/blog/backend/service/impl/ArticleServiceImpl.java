package com.tg.blog.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.exception.ArticleException;
import com.tg.blog.backend.dao.ArticleMapper;
import com.tg.blog.backend.dao.ArticleTagMapper;
import com.tg.blog.backend.dao.CategoryMapper;
import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Article;
import com.tg.blog.backend.entity.Category;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.mapper.ArticleConverter;
import com.tg.blog.backend.mapper.CategoryConverter;
import com.tg.blog.backend.mapper.TagConverter;
import com.tg.blog.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleConverter articleConverter;
    @Autowired
    CategoryConverter categoryConverter;
    @Autowired
    TagConverter tagConverter;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TagMapper tagMapper;

    @Transactional
    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = articleConverter.toEntity(articleDTO);
        articleMapper.insertArticle(article);
        articleTagMapper.batchInsertArticleTags(article.getId(), articleDTO.getTagIds());
        return articleConverter.toDTO(article);
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleMapper.selectArticleById(id);
        if (article == null) {
            throw new ArticleException("Article not found with id: " + id);
        }
        ArticleDTO articleDTO = articleConverter.toDTO(article);
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleDTO.setCategory(categoryConverter.toDTO(category));
        List<Long> tagIds = articleTagMapper.selectByArticleId(article.getId());
        if(tagIds!=null && !tagIds.isEmpty()){
            List<Tag> tags = tagMapper.selectByIds(tagIds);
            articleDTO.setTags(tagConverter.toDTOList(tags));
        }
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleMapper.selectAllArticles();
        if (articles.isEmpty()) {
            throw new ArticleException("No articles found");
        }
        List<ArticleDTO> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = articleConverter.toDTO(article);
            Category category = categoryMapper.selectById(article.getCategoryId());
            dto.setCategory(categoryConverter.toDTO(category));
            List<Long> tagIds = articleTagMapper.selectByArticleId(article.getId());
            if(tagIds!=null && !tagIds.isEmpty()){
                List<Tag> tags = tagMapper.selectByIds(tagIds);
                dto.setTags(tagConverter.toDTOList(tags));
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public PageInfo<ArticleDTO> getArticlesByPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectAllArticles();
        List<ArticleDTO> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = articleConverter.toDTO(article);
            Category category = categoryMapper.selectById(article.getCategoryId());
            dto.setCategory(categoryConverter.toDTO(category));
            List<Long> tagIds = articleTagMapper.selectByArticleId(article.getId());
            if(tagIds!=null && !tagIds.isEmpty()){
                List<Tag> tags = tagMapper.selectByIds(tagIds);
                dto.setTags(tagConverter.toDTOList(tags));
            }
            result.add(dto);
        }
        return new PageInfo<>(result);
    }
    
    @Transactional
    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        Article existingArticle = articleMapper.selectArticleById(id);
        if (existingArticle == null) {
            throw new ArticleException("Cannot update. Article not found with id: " + id);
        }
        Article article = articleConverter.toEntity(articleDTO);
        article.setId(id);
        articleMapper.updateArticle(article);
        articleTagMapper.deleteByArticleId(article.getId());
        articleTagMapper.batchInsertArticleTags(article.getId(), articleDTO.getTagIds());
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
    
    @Override
    public void likeArticle(Long id) {
        articleMapper.incrementLikes(id);
    }
    
    @Override
    public void commentArticle(Long id) {
        articleMapper.incrementComments(id);
    }
    
    @Override
    public void viewArticle(Long id) {
        articleMapper.incrementViews(id);
    }
    
    @Override
    public void updateCommentCount(Long id, Integer count) {
        articleMapper.updateCommentCount(id,count);
    }
    
    @Override
    public List<CategoryStatsDTO> getCategoryStats() {
        return articleMapper.selectCategoryStats();
    }
    
    @Override
    public List<TagStatsDTO> getTagStats() {
        return articleMapper.selectTagStats();
    }
    
    @Override
    public PageInfo<ArticleDTO> getArticlesByCategory(Long categoryId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectArticlesByCategory(categoryId);
        List<ArticleDTO> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = articleConverter.toDTO(article);
            Category category = categoryMapper.selectById(article.getCategoryId());
            dto.setCategory(categoryConverter.toDTO(category));
            List<Long> tagIds = articleTagMapper.selectByArticleId(article.getId());
            if(tagIds!=null && !tagIds.isEmpty()){
                List<Tag> tags = tagMapper.selectByIds(tagIds);
                dto.setTags(tagConverter.toDTOList(tags));
            }
            result.add(dto);
        }
        return new PageInfo<>(result);
    }
    
    @Override
    public PageInfo<ArticleDTO> getArticlesByTag(Long tagId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectArticlesByTag(tagId);
        List<ArticleDTO> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = articleConverter.toDTO(article);
            Category category = categoryMapper.selectById(article.getCategoryId());
            dto.setCategory(categoryConverter.toDTO(category));
            List<Long> tagIds = articleTagMapper.selectByArticleId(article.getId());
            if(tagIds!=null && !tagIds.isEmpty()){
                List<Tag> tags = tagMapper.selectByIds(tagIds);
                dto.setTags(tagConverter.toDTOList(tags));
            }
            result.add(dto);
        }
        return new PageInfo<>(result);
    }
    
}
