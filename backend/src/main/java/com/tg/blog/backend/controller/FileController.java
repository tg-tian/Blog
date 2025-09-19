package com.tg.blog.backend.controller;

import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.dto.UploadUrlDTO;
import com.tg.blog.backend.service.impl.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件控制器
 * 提供文件上传、下载相关的REST API接口
 * 
 * @author TG
 * @since 1.0.0
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioService minioService;

    /**
     * 获取文件上传URL
     * @param params 请求参数，包含prefix(路径前缀)和filename(文件名)
     * @return 文件上传URL信息
     */
    @PostMapping("/getUploadUrl")
    public ResponseEntity<UploadUrlDTO> getUploadUrl(@RequestBody Map<String, String> params) {
        String prefix = params.get("prefix");
        String fileName = params.get("filename");
        UploadUrlDTO dto = minioService.getUploadUrl(prefix, fileName);
        return ResponseEntity.success(dto);
    }

    /**
     * 获取文件预签名访问URL
     * @param params 请求参数，包含objectName(对象名称)
     * @return 文件预签名访问URL信息
     */
    @PostMapping("/getPresignedUrl")
    public ResponseEntity<UploadUrlDTO> getPresignedUrl(@RequestBody Map<String, String> params) {
        String objectName = params.get("objectName");
        UploadUrlDTO dto = minioService.getPresignedUrl(objectName);
        return ResponseEntity.success(dto);
    }
}
