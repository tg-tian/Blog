package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.UploadUrlDTO;

public interface FileService {
    UploadUrlDTO getUploadUrl(String prefix, String fileName);
}
