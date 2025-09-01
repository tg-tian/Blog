package com.tg.blog.backend.controller;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.SiteStatsDTO;
import com.tg.blog.backend.service.SiteStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/site-stats")
public class SiteStatsController {

    @Autowired
    SiteStatsService siteStatsService;

    @GetMapping("/all")
    public ResponseEntity<SiteStatsDTO> getAllStats() {
        SiteStatsDTO siteStats = siteStatsService.getSiteStats();
        return ResponseEntity.success(siteStats);
    }
    
    @PostMapping("/visits/increment")
    public ResponseEntity<String> incrementVisits() {
        siteStatsService.incrementVisits();
        return ResponseEntity.success("访问量增加成功");
    }
    
   
    @PostMapping("/likes/increment")
    public ResponseEntity<String> incrementLikes() {
        siteStatsService.incrementLikes();
        return ResponseEntity.success("点赞量增加成功");
    }
    
}
