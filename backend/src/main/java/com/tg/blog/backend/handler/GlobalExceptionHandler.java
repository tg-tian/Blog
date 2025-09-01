package com.tg.blog.backend.handler;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.common.exception.ArticleException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.failure(500, ex.getMessage());
    }
}
