package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.TagDTO;
import java.util.List;

/**
 * 标签服务接口
 * 提供标签相关的业务逻辑操作
 * 
 * @author TG
 * @since 1.0.0
 */
public interface TagService {
    
    /**
     * 根据ID获取标签信息
     * @param id 标签ID
     * @return 标签信息
     */
    TagDTO getById(Long id);
    
    /**
     * 获取所有标签列表
     * @return 标签列表
     */
    List<TagDTO> getAll();
    
    /**
     * 添加新标签
     * @param tagDTO 标签信息
     */
    void add(TagDTO tagDTO);
    
    /**
     * 更新标签信息
     * @param tagDTO 标签信息
     */
    void update(TagDTO tagDTO);
    
    /**
     * 删除标签
     * @param id 标签ID
     */
    void delete(Long id);
}
