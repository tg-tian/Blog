package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.UploadUrlDTO;

/**
 * 文件服务接口
 * 提供文件上传、下载等相关操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface FileService {
    
    /**
     * 获取文件上传URL
     * @param prefix 文件路径前缀
     * @param fileName 文件名
     * @return 上传URL信息
     */
    UploadUrlDTO getUploadUrl(String prefix, String fileName);
}
