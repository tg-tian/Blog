package com.tg.blog.backend.dao;

import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章数据访问接口
 * 提供文章相关的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface ArticleMapper {
    int insertArticle(Article article);
    
    Article selectArticleById(Long id);
    
    List<Article> selectAllArticles();
    
    int updateArticle(Article article);
    
    int deleteArticle(Long id);

    int incrementLikes(Long id);

    int incrementComments(Long id);

    int incrementViews(Long id);
    
    void updateCommentCount(Long id, Integer count);
    
    Long countTotalArticles();
    
    List<CategoryStatsDTO> selectCategoryStats();
    
    List<TagStatsDTO> selectTagStats();
    
    List<Article> selectArticlesByCategory(Long categoryId);
    
    List<Article> selectArticlesByTag(Long tagId);
}
