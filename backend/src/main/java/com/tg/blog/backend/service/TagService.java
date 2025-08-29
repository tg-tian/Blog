package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.TagDTO;
import java.util.List;

public interface TagService {
    TagDTO getById(Long id);
    List<TagDTO> getAll();
    void add(TagDTO tagDTO);
    void update(TagDTO tagDTO);
    void delete(Long id);
}
