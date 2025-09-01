package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.SiteStatsDTO;

import java.util.Map;

public interface SiteStatsService {

    void incrementVisits();

    void incrementLikes();

    void incrementTodayVisits();

    void incrementTodayLikes();

    SiteStatsDTO getSiteStats();
    
    Long getTotalArticles();
    
    Long getUptime();
}
