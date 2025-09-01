package com.tg.blog.backend.dao;

import com.tg.blog.backend.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ArticleTagMapper {
    int insertArticleTag(Long articleId,Long tagId);
    
    int batchInsertArticleTags(Long articleId,List<Long> tagIds);
    
    
    List<Long> selectByArticleId(Long articleId);
    
    int deleteByArticleId(Long articleId);
    
    int deleteArticleTag(Long articleId, Long tagId);
    
    
}
