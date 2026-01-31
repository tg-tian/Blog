package com.tg.blog.backend.entity;

import lombok.Data;

@Data
public class ArticleTagRelation {
    private Long articleId;
    private Long tagId;
}
