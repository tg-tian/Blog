package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 文章标签关联数据访问接口
 * 提供文章和标签多对多关系的数据库操作方法
 * 
 * @author TG
 * @since 1.0.0
 */
@Mapper
public interface ArticleTagMapper {
    
    /**
     * 插入文章标签关联记录
     * @param articleId 文章ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    int insertArticleTag(Long articleId, Long tagId);
    
    /**
     * 批量插入文章标签关联记录
     * @param articleId 文章ID
     * @param tagIds 标签ID列表
     * @return 影响的行数
     */
    int batchInsertArticleTags(Long articleId, List<Long> tagIds);
    
    /**
     * 根据文章ID查询关联的标签ID列表
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    List<Long> selectByArticleId(Long articleId);
    
    /**
     * 根据文章ID删除所有关联的标签记录
     * @param articleId 文章ID
     * @return 影响的行数
     */
    int deleteByArticleId(Long articleId);
    
    /**
     * 删除指定文章和标签的关联记录
     * @param articleId 文章ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    int deleteArticleTag(Long articleId, Long tagId);
}
