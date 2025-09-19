package com.tg.blog.backend.common.constants;

/**
 * Redis键名常量类
 * 定义系统中使用的所有Redis键名
 * 
 * @author TG
 * @since 1.0.0
 */
public class RedisKeys {
    
    // ==================== 网站统计相关 ====================
    
    /** 网站总访问量 */
    public static final String SITE_TOTAL_VISITS = "site:visits:total";
    
    /** 网站总点赞量 */
    public static final String SITE_TOTAL_LIKES = "site:likes:total";
    
    /** 网站文章总数 */
    public static final String SITE_TOTAL_ARTICLES = "site:articles:total";
    
    /** 网站运行时长（天数） */
    public static final String SITE_START_TIME = "site:start-time";
    
    // ==================== 每日统计相关 ====================
    
    /** 每日访问量，需要拼接日期，格式：site:visits:daily:yyyy-MM-dd */
    public static final String SITE_DAILY_VISITS = "site:visits:daily:";
    
    /** 每日点赞量，需要拼接日期，格式：site:likes:daily:yyyy-MM-dd */
    public static final String SITE_DAILY_LIKES = "site:likes:daily:";
}
