package com.mirrormetech.corm.core.post.infra.persistence;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.domain.repository.PostRepository;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 仓储层 下水道逻辑
 * @author spencer
 * @date 2025/05/06
 */
@Repository
@Mapper
@RequiredArgsConstructor
public class MyBatisPostRepository  implements PostRepository {

    private final PostMapper postMapper;

    @Override
    public void createPost(PostDO postDO) {
        postMapper.insert(postDO);
    }

    @Override
    public Page<PostDO> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size) {

        return postMapper.selectPosts(new Page<>(pageNum, size), queryListDTO);
    }

}
