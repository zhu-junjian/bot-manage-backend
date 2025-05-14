package com.mirrormetech.corm.core.post.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;

public interface PostRepository {

    void createPost(PostDO postDO);

    Page<PostDO> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size);
}
