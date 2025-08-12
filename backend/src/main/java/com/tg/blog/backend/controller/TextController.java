package com.tg.blog.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextController {
    
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
}
