package com.tg.blog.backend.service;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;

import java.util.List;

/**
 * 文章服务接口
 * 提供文章相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface ArticleService {

    /**
     * 创建新文章
     * @param articleDTO 文章数据传输对象
     * @return 创建成功的文章信息
     */
    ArticleDTO createArticle(ArticleDTO articleDTO);

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章详情信息
     */
    ArticleDTO getArticleById(Long id);

    /**
     * 获取所有文章列表
     * @return 文章列表
     */
    List<ArticleDTO> getAllArticles();

    /**
     * 更新文章信息
     * @param id 文章ID
     * @param articleDTO 更新的文章数据
     * @return 更新后的文章信息
     */
    ArticleDTO updateArticle(Long id, ArticleDTO articleDTO);

    /**
     * 删除文章
     * @param id 文章ID
     * @return 删除是否成功
     */
    boolean deleteArticle(Long id);

    /**
     * 分页获取文章列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页文章信息
     */
    PageInfo<ArticleDTO> getArticlesByPage(int page, int size);

    /**
     * 文章点赞
     * @param id 文章ID
     */
    void likeArticle(Long id);

    /**
     * 文章评论数增加
     * @param id 文章ID
     */
    void commentArticle(Long id);

    /**
     * 文章浏览量增加
     * @param id 文章ID
     */
    void viewArticle(Long id);
    
    /**
     * 更新文章评论数
     * @param id 文章ID
     * @param count 评论数量
     */
    void updateCommentCount(Long id, Integer count);
    
    /**
     * 获取分类统计信息
     * @return 分类及其文章数量统计列表
     */
    List<CategoryStatsDTO> getCategoryStats();
    
    /**
     * 根据分类分页获取文章列表
     * @param categoryId 分类ID
     * @param page 页码
     * @param size 每页大小
     * @return 分页文章信息
     */
    PageInfo<ArticleDTO> getArticlesByCategory(Long categoryId, int page, int size);
    
    /**
     * 根据标签分页获取文章列表
     * @param tagId 标签ID
     * @param page 页码
     * @param size 每页大小
     * @return 分页文章信息
     */
    PageInfo<ArticleDTO> getArticlesByTag(Long tagId, int page, int size);
    
    /**
     * 获取标签统计信息
     * @return 标签及其文章数量统计列表
     */
    List<TagStatsDTO> getTagStats();
}
