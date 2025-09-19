package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 文件上传URL数据传输对象
 * 用于传输文件上传相关的URL信息
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class UploadUrlDTO {
    
    /** 对象名称，文件在存储中的唯一标识 */
    private String objectName;
    
    /** 上传URL，用于文件上传 */
    private String uploadUrl;
    
    /** 预签名URL，用于文件访问 */
    private String presignedUrl;
}

