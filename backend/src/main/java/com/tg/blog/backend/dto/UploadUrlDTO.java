package com.tg.blog.backend.dto;

import lombok.Data;

@Data
public class UploadUrlDTO {
    private String objectName;
    private String uploadUrl;
    private String presignedUrl;
}

