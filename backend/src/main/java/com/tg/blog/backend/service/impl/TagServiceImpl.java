package com.tg.blog.backend.service.impl;

import com.tg.blog.backend.dao.TagMapper;
import com.tg.blog.backend.dto.TagDTO;
import com.tg.blog.backend.entity.Tag;
import com.tg.blog.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public TagDTO getById(Long id) {
        return tagMapper.selectById(id);
    }

    @Override
    public List<TagDTO> getAll() {
        return tagMapper.selectAll();
    }

    @Override
    public void add(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setSlug(tagDTO.getSlug());
        tag.setDescription(tagDTO.getDescription());
        tagMapper.insert(tag);
    }

    @Override
    public void update(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        tag.setSlug(tagDTO.getSlug());
        tag.setDescription(tagDTO.getDescription());
        tagMapper.update(tag);
    }

    @Override
    public void delete(Long id) {
        tagMapper.deleteById(id);
    }
}
