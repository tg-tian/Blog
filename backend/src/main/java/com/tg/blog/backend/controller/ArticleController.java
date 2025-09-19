package com.tg.blog.backend.controller;

import com.github.pagehelper.PageInfo;
import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.dto.CategoryStatsDTO;
import com.tg.blog.backend.dto.TagStatsDTO;
import com.tg.blog.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.tg.blog.backend.common.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文章控制器
 * 提供文章相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 创建新文章
     * @param articleDTO 文章数据传输对象，包含文章的所有信息
     * @return 创建成功的文章信息
     */
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO article =  articleService.createArticle(articleDTO);
        return ResponseEntity.success(article);
    }

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章详情信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ResponseEntity.success(article);
    }

    /**
     * 获取所有文章列表
     * @return 所有文章的列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.success(articles);
    }

    /**
     * 分页获取文章列表
     * @param page 页码，从1开始
     * @param size 每页显示的文章数量
     * @return 分页后的文章列表
     */
    @GetMapping
    public ResponseEntity<PageInfo<ArticleDTO>> getArticlesByPage(@RequestParam int page, @RequestParam int size) {
        PageInfo<ArticleDTO> articles = articleService.getArticlesByPage(page, size);
        return ResponseEntity.success(articles);
    }
    
    /**
     * 更新文章信息
     * @param id 文章ID
     * @param articleDTO 更新的文章数据
     * @return 更新后的文章信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        ArticleDTO article = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.success(article);
    }

    /**
     * 删除文章
     * @param id 文章ID
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.success("Article deleted successfully");
    }

    /**
     * 文章点赞
     * @param id 文章ID
     * @return 点赞操作结果
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeArticle(@PathVariable Long id) {
        articleService.likeArticle(id);
        return ResponseEntity.success("Article liked successfully");
    }

    /**
     * 文章评论数增加
     * @param id 文章ID
     * @return 评论操作结果
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<String> commentArticle(@PathVariable Long id) {
        articleService.commentArticle(id);
        return ResponseEntity.success("Article commented successfully");
    }

    /**
     * 增加文章浏览量
     * @param id 文章ID
     * @return 浏览量更新结果
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<String> incrementViewCount(@PathVariable Long id) {
        articleService.viewArticle(id);
        return ResponseEntity.success("View count incremented successfully");
    }

    /**
     * 更新文章评论数
     * @param id 文章ID
     * @param request 包含评论数量的请求体
     * @return 更新操作结果
     */
    @PutMapping("/{id}/comments")
    public ResponseEntity<String> updateCommentCount(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer count = request.get("count");
        articleService.updateCommentCount(id, count);
        return ResponseEntity.success("Comment count updated successfully");
    }
    
    /**
     * 获取所有分类及其文章数量统计
     * @return 分类统计信息列表，包含分类名称和对应的文章数量
     */
    @GetMapping("/categories/stats")
    public ResponseEntity<List<CategoryStatsDTO>> getCategoryStats() {
        List<CategoryStatsDTO> categoryStats = articleService.getCategoryStats();
        return ResponseEntity.success(categoryStats);
    }
    
    /**
     * 获取所有标签及其文章数量统计
     * @return 标签统计信息列表，包含标签名称和对应的文章数量
     */
    @GetMapping("/tags/stats")
    public ResponseEntity<List<TagStatsDTO>> getTagStats() {
        List<TagStatsDTO> tagStats = articleService.getTagStats();
        return ResponseEntity.success(tagStats);
    }
    
    /**
     * 根据分类ID分页获取文章列表
     * @param categoryId 分类ID
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @return 指定分类下的分页文章列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageInfo<ArticleDTO>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<ArticleDTO> articles = articleService.getArticlesByCategory(categoryId, page, size);
        return ResponseEntity.success(articles);
    }
    
    /**
     * 根据标签ID分页获取文章列表
     * @param tagId 标签ID
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @return 指定标签下的分页文章列表
     */
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<PageInfo<ArticleDTO>> getArticlesByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<ArticleDTO> articles = articleService.getArticlesByTag(tagId, page, size);
        return ResponseEntity.success(articles);
    }
}
