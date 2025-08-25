package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.UploadUrlDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadUrlDTO getUploadUrl(String prefix, String fileName);
}
