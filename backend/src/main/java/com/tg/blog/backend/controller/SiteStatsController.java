package com.tg.blog.backend.controller;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.SiteStatsDTO;
import com.tg.blog.backend.service.SiteStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站统计控制器
 * 提供网站统计相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/site-stats")
public class SiteStatsController {

    @Autowired
    SiteStatsService siteStatsService;

    /**
     * 获取网站所有统计信息
     * @return 网站统计数据，包含访问量、点赞量、文章数等信息
     */
    @GetMapping("/all")
    public ResponseEntity<SiteStatsDTO> getAllStats() {
        SiteStatsDTO siteStats = siteStatsService.getSiteStats();
        return ResponseEntity.success(siteStats);
    }
    
    /**
     * 增加网站访问量
     * @return 访问量增加操作结果
     */
    @PostMapping("/visits/increment")
    public ResponseEntity<String> incrementVisits() {
        siteStatsService.incrementVisits();
        return ResponseEntity.success("访问量增加成功");
    }
    
    /**
     * 增加网站点赞量
     * @return 点赞量增加操作结果
     */
    @PostMapping("/likes/increment")
    public ResponseEntity<String> incrementLikes() {
        siteStatsService.incrementLikes();
        return ResponseEntity.success("点赞量增加成功");
    }
    
}
