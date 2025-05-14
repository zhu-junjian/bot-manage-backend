package com.mirrormetech.corm.core.post.domain;

import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Component
@Slf4j
public class PostFactory {

    /**
     * 通过DTO创建POST
     * @param postDTO
     * @return
     */
    public Post create(PostDTO postDTO) {
        Post newPost = new Post();
        newPost.createPost(postDTO);
        return newPost;
    }

    public PostDO createDO(Post post) {
        PostDO postDO = new PostDO();
        BeanUtils.copyProperties(post, postDO);
        return postDO;
    }

    public void DTO2Post(PostDTO postDTO, Post post) {}

    public void DO2Post(PostDO postDTO, Post post) {
        BeanUtils.copyProperties(postDTO, post);
    }
}
