package com.tg.blog.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 * 提供应用程序健康状态检查的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
public class HealthController {
    
    /**
     * 健康检查接口
     * @return 返回"pong"表示应用程序正常运行
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
}
