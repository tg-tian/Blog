package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.config.MinioConfig;
import com.tg.blog.backend.dto.UploadUrlDTO;
import com.tg.blog.backend.service.FileService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MinioService implements FileService {
    private static final String COVER_PREFIX = "cover/";

    @Autowired
    private MinioConfig minioConfig;

    private MinioClient buildMinioClient() {
        return MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                .build();
    }

    private String generatePresignedUrl(String objectName, io.minio.http.Method method) {
        MinioClient minioClient = buildMinioClient();
        try {
            return minioClient.getPresignedObjectUrl(
                    io.minio.GetPresignedObjectUrlArgs.builder()
                            .method(method)
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .expiry(60 * 60)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取预签名地址失败", e);
        }
    }

    public UploadUrlDTO getUploadUrl(String prefix, String fileName) {
        String uuid = UUID.randomUUID().toString();
        String objectName = (prefix != null ? prefix : "") + "/" + fileName + "-" + uuid;
        String url = generatePresignedUrl(objectName, io.minio.http.Method.PUT);
        UploadUrlDTO uploadUrlDTO = new UploadUrlDTO();
        uploadUrlDTO.setUploadUrl(url);
        uploadUrlDTO.setObjectName(objectName);
        return uploadUrlDTO;
    }

    public UploadUrlDTO getPresignedUrl(String objectName) {
        String url = generatePresignedUrl(objectName, io.minio.http.Method.GET);
        UploadUrlDTO uploadUrlDTO = new UploadUrlDTO();
        uploadUrlDTO.setPresignedUrl(url);
        return uploadUrlDTO;
    }
}
