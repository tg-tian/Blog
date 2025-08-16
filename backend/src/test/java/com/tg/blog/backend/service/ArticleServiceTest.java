package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.ArticleDTO;
import com.tg.blog.backend.entity.Article;
import com.tg.blog.backend.mapper.ArticleConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleConverter articleConverter;

    @Test
    void testArticleConversion() {
        // 创建一个 ArticleDTO 对象
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Test Article");
        articleDTO.setContent("This is a test article content.");

        // 使用 ArticleConverter 将 DTO 转换为 Entity
        Article article = articleConverter.toEntity(articleDTO);

        // 断言转换后的 Entity 不为 null
        assert article != null : "Article entity should not be null after conversion";

        // 使用 ArticleConverter 将 Entity 转换回 DTO
        ArticleDTO convertedDTO = articleConverter.toDTO(article);

        // 断言转换回的 DTO 与原始 DTO 相同
        assert convertedDTO.getTitle().equals(articleDTO.getTitle()) : "Titles should match after conversion";
        assert convertedDTO.getContent().equals(articleDTO.getContent()) : "Contents should match after conversion";
    }

}
