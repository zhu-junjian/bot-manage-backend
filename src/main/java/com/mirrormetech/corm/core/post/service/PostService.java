package com.mirrormetech.corm.core.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    Post create(PostDTO postDTO);

    Page<Post> getAllPosts(QueryListDTO queryListDTO, Page page);
}
