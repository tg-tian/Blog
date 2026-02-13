package com.tg.blog.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.common.exception.BusinessException;
import com.tg.blog.backend.dao.ArticleMapper;
import com.tg.blog.backend.dao.ArticleTagMapper;
import com.tg.blog.backend.dao.CategoryMapper;
import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Article;
import com.tg.blog.backend.entity.ArticleTagRelation;
import com.tg.blog.backend.entity.Category;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.mapper.ArticleConverter;
import com.tg.blog.backend.mapper.CategoryConverter;
import com.tg.blog.backend.mapper.TagConverter;
import com.tg.blog.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文章服务实现类
 * 实现文章相关的业务逻辑，包括CRUD操作、标签关联管理和统计功能
 * 
 * @author TG
 * @since 1.0.0
 */
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
            throw new BusinessException(404, "Article not found with id: " + id);
        }
        List<ArticleDTO> result = buildArticleDTOs(Collections.singletonList(article));
        return result.get(0);
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleMapper.selectAllArticles();
        if (articles.isEmpty()) {
            throw new BusinessException(404, "No articles found");
        }
        return buildArticleDTOs(articles);
    }

    @Override
    public PageInfo<ArticleDTO> getArticlesByPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectAllArticles();
        return new PageInfo<>(buildArticleDTOs(articles));
    }
    
    @Transactional
    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        Article existingArticle = articleMapper.selectArticleById(id);
        if (existingArticle == null) {
            throw new BusinessException(404, "Cannot update. Article not found with id: " + id);
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
            throw new BusinessException(500, "Failed to delete article with id: " + id);
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
        return new PageInfo<>(buildArticleDTOs(articles));
    }
    
    @Override
    public PageInfo<ArticleDTO> getArticlesByTag(Long tagId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Article> articles = articleMapper.selectArticlesByTag(tagId);
        return new PageInfo<>(buildArticleDTOs(articles));
    }

    private List<ArticleDTO> buildArticleDTOs(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> categoryIds = articles.stream().map(Article::getCategoryId).toList();
        List<Long> articleIds = articles.stream().map(Article::getId).toList();
        Map<Long, Category> categoryMap = categoryIds.isEmpty()
                ? Map.of()
                : categoryMapper.selectByIds(categoryIds).stream()
                .collect(java.util.stream.Collectors.toMap(Category::getId, category -> category));
        Map<Long, List<Long>> tagIdsByArticleId = new HashMap<>();
        if (!articleIds.isEmpty()) {
            List<ArticleTagRelation> relations = articleTagMapper.selectArticleTagByArticleIds(articleIds);
            for (ArticleTagRelation relation : relations) {
                if (relation == null || relation.getArticleId() == null || relation.getTagId() == null) {
                    continue;
                }
                tagIdsByArticleId
                        .computeIfAbsent(relation.getArticleId(), key -> new ArrayList<>())
                        .add(relation.getTagId());
            }
        }
        Set<Long> tagIds = tagIdsByArticleId.values().stream().flatMap(List::stream)
                .collect(java.util.stream.Collectors.toSet());
        Map<Long, Tag> tagMap = tagIds.isEmpty()
                ? Map.of()
                : tagMapper.selectByIds(new ArrayList<>(tagIds)).stream()
                .collect(java.util.stream.Collectors.toMap(Tag::getId, tag -> tag));
        List<ArticleDTO> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO dto = articleConverter.toDTO(article);
            Category category = categoryMap.get(article.getCategoryId());
            if (category != null) {
                dto.setCategory(categoryConverter.toDTO(category));
            }
            List<Long> tagIdList = tagIdsByArticleId.get(article.getId());
            if (tagIdList != null && !tagIdList.isEmpty()) {
                List<Tag> tags = new ArrayList<>();
                for (Long tagId : tagIdList) {
                    Tag tag = tagMap.get(tagId);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                if (!tags.isEmpty()) {
                    dto.setTags(tagConverter.toDTOList(tags));
                }
            }
            result.add(dto);
        }
        return result;
    }
    
}
