package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class SiteStatsDTO{

    private Long totalVisits;
    private Long totalLikes;
    private Long totalArticles;
    private Long uptime;
    private Long todayVisits;
    private Long todayLikes;
}
