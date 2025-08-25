package com.tg.blog.backend.controller;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.UploadUrlDTO;
import com.tg.blog.backend.service.impl.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioService minioService;

    @PostMapping("/getUploadUrl")
    public ResponseEntity<UploadUrlDTO> getUploadUrl(@RequestBody Map<String, String> params) {
        String prefix = params.get("prefix");
        String fileName = params.get("filename");
        UploadUrlDTO dto = minioService.getUploadUrl(prefix, fileName);
        return ResponseEntity.success(dto);
    }

    @PostMapping("/getPresignedUrl")
    public ResponseEntity<UploadUrlDTO> getPresignedUrl(@RequestBody Map<String, String> params) {
        String objectName = params.get("objectName");
        UploadUrlDTO dto = minioService.getPresignedUrl(objectName);
        return ResponseEntity.success(dto);
    }
}
