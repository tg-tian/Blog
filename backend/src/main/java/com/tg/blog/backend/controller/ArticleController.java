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

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO article =  articleService.createArticle(articleDTO);
        return ResponseEntity.success(article);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ResponseEntity.success(article);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.success(articles);
    }

    @GetMapping
    public ResponseEntity<PageInfo<ArticleDTO>> getArticlesByPage(@RequestParam int page, @RequestParam int size) {
        PageInfo<ArticleDTO> articles = articleService.getArticlesByPage(page, size);
        return ResponseEntity.success(articles);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        ArticleDTO article = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.success(article);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.success("Article deleted successfully");
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeArticle(@PathVariable Long id) {
        articleService.likeArticle(id);
        return ResponseEntity.success("Article liked successfully");
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<String> commentArticle(@PathVariable Long id) {
        articleService.commentArticle(id);
        return ResponseEntity.success("Article commented successfully");
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<String> incrementViewCount(@PathVariable Long id) {
        articleService.viewArticle(id);
        return ResponseEntity.success("View count incremented successfully");
    }

    @PutMapping("/{id}/comments")
    public ResponseEntity<String> updateCommentCount(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer count = request.get("count");
        articleService.updateCommentCount(id, count);
        return ResponseEntity.success("Comment count updated successfully");
    }
    
    /**
     * 获取所有分类及其文章数量统计
     */
    @GetMapping("/categories/stats")
    public ResponseEntity<List<CategoryStatsDTO>> getCategoryStats() {
        List<CategoryStatsDTO> categoryStats = articleService.getCategoryStats();
        return ResponseEntity.success(categoryStats);
    }
    
    /**
     * 获取所有标签及其文章数量统计
     */
    @GetMapping("/tags/stats")
    public ResponseEntity<List<TagStatsDTO>> getTagStats() {
        List<TagStatsDTO> tagStats = articleService.getTagStats();
        return ResponseEntity.success(tagStats);
    }
    
    /**
     * 根据分类ID获取文章列表
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
     * 根据标签ID获取文章列表
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
