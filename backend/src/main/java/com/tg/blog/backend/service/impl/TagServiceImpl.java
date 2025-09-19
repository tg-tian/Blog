package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.mapper.TagConverter;
import com.tg.blog.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 标签服务实现类
 * 实现标签相关的业务逻辑
 * 
 * @author TG
 * @since 1.0.0
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TagConverter tagConverter;

    @Override
    public TagDTO getById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return tagConverter.toDTO(tag);
    }

    @Override
    public List<TagDTO> getAll() {
        List<Tag> tags = tagMapper.selectAll();
        return tagConverter.toDTOList(tags);
    }

    @Override
    public void add(TagDTO tagDTO) {
        Tag tag = tagConverter.toEntity(tagDTO);
        tagMapper.insert(tag);
    }

    @Override
    public void update(TagDTO tagDTO) {
        Tag tag = tagConverter.toEntity(tagDTO);
        tagMapper.update(tag);
    }

    @Override
    public void delete(Long id) {
        tagMapper.deleteById(id);
    }
}

