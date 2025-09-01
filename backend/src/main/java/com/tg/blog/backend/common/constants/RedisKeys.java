package com.tg.blog.backend.common.constants;

public class RedisKeys {
    // 网站统计
    public static final String SITE_TOTAL_VISITS = "site:visits:total";
    
    public static final String SITE_TOTAL_LIKES = "site:likes:total";
    
    public static final String SITE_TOTAL_ARTICLES = "site:articles:total";
    
    public static final String SITE_START_TIME = "site:start-time";
    
    // 今日统计（hash or key）
    public static final String SITE_DAILY_VISITS = "site:visits:daily:";
    
    public static final String SITE_DAILY_LIKES = "site:likes:daily:";
}
