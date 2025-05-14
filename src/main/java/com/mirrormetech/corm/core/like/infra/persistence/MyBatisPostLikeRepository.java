package com.mirrormetech.corm.core.like.infra.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.domain.dto.QueryPostLikeDTO;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Repository
@RequiredArgsConstructor
public class MyBatisPostLikeRepository implements PostLikeRepository {

    private final PostLikeMapper postLikeMapper;

    @Override
    public List<PostLikeDO> selectList(QueryWrapper<PostLikeDO> wrapper) {
        return postLikeMapper.selectList(wrapper);
    }

    /**
     * 通过user_id & post_id的唯一键 判断status = 1-status
     * @param postLikeDTO
     * @return
     */
    @Override
    public int likeOrUnlike(PostLikeDTO postLikeDTO) {
        return postLikeMapper.likeOrUnlike(postLikeDTO.getUserId(), postLikeDTO.getPostId());
    }

    @Override
    public PostLikeDO findByPostId(QueryPostLikeDTO queryPostLikeDTO) {
        return postLikeMapper.selectOne(new QueryWrapper<PostLikeDO>()
                .eq("user_id", queryPostLikeDTO.getUserId())
                .eq("post_id",queryPostLikeDTO.getPostId()));
    }

    @Override
    public void save(PostLikeDO postLikeDO) {
        postLikeMapper.insert(postLikeDO);
    }

    @Override
    public void update(PostLikeDO postLikeDO) {
        postLikeMapper.update(new UpdateWrapper<>(postLikeDO));
    }
}
