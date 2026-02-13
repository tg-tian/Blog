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

    // ==================== 认证相关 ====================
    /** 用户登录token，按用户维度存储，格式：auth:token:user:<username> */
    public static final String AUTH_USER_TOKEN_PREFIX = "auth:token:user:";

    /** 注册邮箱验证码，格式：register:email:code:<email> */
    public static final String REGISTER_EMAIL_CODE_PREFIX = "register:email:code:";

    /** 注册邮箱发送频率限制，格式：register:email:send:lock:<email> */
    public static final String REGISTER_EMAIL_SEND_LOCK_PREFIX = "register:email:send:lock:";

    /** 注册邮箱验证码校验次数，格式：register:email:verify:count:<email> */
    public static final String REGISTER_EMAIL_VERIFY_COUNT_PREFIX = "register:email:verify:count:";

    /** 注册token，格式：register:token:<token> */
    public static final String REGISTER_TOKEN_PREFIX = "register:token:";
}
