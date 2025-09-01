package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.config.SiteConfig;
import com.tg.blog.backend.dao.ArticleMapper;
import com.tg.blog.backend.dto.SiteStatsDTO;
import com.tg.blog.backend.service.SiteStatsService;
import com.tg.blog.backend.service.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


@Service
public class SiteStatsServiceImpl implements SiteStatsService {
    
    @Autowired
    RedisService redisService;
    
    @Autowired
    ArticleMapper articleMapper;
    
    @Autowired
    SiteConfig siteConfig;
    
    /**
     * 获取当前日期字符串，格式：yyyy-MM-dd
     */
    private String getCurrentDateString() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    @Override
    public void incrementVisits() {
        redisService.increment(RedisKeys.SITE_TOTAL_VISITS, 1);
        incrementTodayVisits();
    }

    @Override
    public void incrementLikes() {
        redisService.increment(RedisKeys.SITE_TOTAL_LIKES, 1);
        incrementTodayLikes();
    }

    @Override
    public void incrementTodayVisits() {
        String todayKey = RedisKeys.SITE_DAILY_VISITS + getCurrentDateString();
        redisService.increment(todayKey, 1);
    }

    @Override
    public void incrementTodayLikes() {
        String todayKey = RedisKeys.SITE_DAILY_LIKES + getCurrentDateString();
        redisService.increment(todayKey, 1);
    }
    
    

    @Override
    public SiteStatsDTO getSiteStats() {
        SiteStatsDTO siteStatsDTO = new SiteStatsDTO();
        String todayDateString = getCurrentDateString();
        siteStatsDTO.setTodayLikes(Long.parseLong(redisService.get(RedisKeys.SITE_DAILY_LIKES + todayDateString)));
        siteStatsDTO.setTodayVisits(Long.parseLong(redisService.get(RedisKeys.SITE_DAILY_VISITS + todayDateString)));
        siteStatsDTO.setTotalLikes(Long.parseLong(redisService.get(RedisKeys.SITE_TOTAL_LIKES)));
        siteStatsDTO.setTotalVisits(Long.parseLong(redisService.get(RedisKeys.SITE_TOTAL_VISITS)));
        siteStatsDTO.setTotalArticles(getTotalArticles());
        siteStatsDTO.setUptime(getUptime());
        return siteStatsDTO;
    }
    
    @Override
    public Long getTotalArticles() {
        String cachedValue = redisService.get(RedisKeys.SITE_TOTAL_ARTICLES);
        if (cachedValue != null && !"0".equals(cachedValue)) {
            return Long.parseLong(cachedValue);
        }
        Long totalArticles = articleMapper.countTotalArticles();
        redisService.set(RedisKeys.SITE_TOTAL_ARTICLES, totalArticles.toString(), 1, TimeUnit.DAYS);
        return totalArticles;
    }
    
    @Override
    public Long getUptime() {
        String cachedUptime = redisService.get(RedisKeys.SITE_START_TIME);
        if (cachedUptime != null && !"0".equals(cachedUptime)) {
            return Long.parseLong(cachedUptime);
        }
        try {
            LocalDateTime creationDateTime = LocalDateTime.parse(siteConfig.getCreationDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDate creationDate = creationDateTime.toLocalDate();
            LocalDate currentDate = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(creationDate, currentDate);
            redisService.set(RedisKeys.SITE_START_TIME, String.valueOf(daysBetween), 1, TimeUnit.DAYS);
            return daysBetween;
        } catch (Exception e) {
            return 0L;
        }
    }
}

