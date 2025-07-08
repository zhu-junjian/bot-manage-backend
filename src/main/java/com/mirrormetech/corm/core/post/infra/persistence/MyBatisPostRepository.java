package com.mirrormetech.corm.core.post.infra.persistence;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.domain.repository.PostRepository;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import com.mirrormetech.corm.core.user.domain.FollowRelation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    /**
     * 根据发布者的userId更新其作品的发布者昵称
     * @param userId 发布者用户ID
     * @param nickName 作品的昵称
     */
    public int updatePosterNickNameByUserId(Long userId, String nickName) {
        return postMapper.updatePosterNickName(userId, nickName);
    }

    /**
     * 判断内容是否存在
     * @param postId 内容ID
     * @return 是否存在
     */
    @Override
    public Boolean isExist(Long postId) {
        return postMapper.selectById(postId) != null;
    }

    @Override
    public void createPost(PostDO postDO) {
        postMapper.insert(postDO);
    }

    @Override
    public Page<PostDO> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size) {

        return postMapper.selectPostDOs(new Page<>(pageNum, size), queryListDTO);
    }

    @Override
    public Page<Post> getAllPostsByCondition(QueryListDTO queryListDTO, Page page) {
        return postMapper.selectPosts(page, queryListDTO);
    }

    /**
     * 根据用户ID查询其作品 按照 发布事件倒序排序
     * @param userId 用户ID
     * @return 作品集合
     */
    @Override
    public Page<PostDO> selectUserWorksByUserId(Page<Object> page, Long userId) {
        return postMapper.selectUserWorksByUserId(page, userId);
    }

    /**
     * 根据用户ID查询其喜欢的作品 按照 发布事件倒序排序
     * @param userId 用户ID
     * @return 作品集合
     */
    @Override
    public Page<PostDO> selectUserLikedWorksByUserId(Page<Object> page, Long userId) {
        /*
            1.查询目标用户喜欢的作品列表
            2.获取这部分作品的详情以及点赞数
            3.查询源用户和 这部分作品的点赞关系
         */
        List<Long> userLikedPostId = postMapper.selectPostIdByUserId(page, userId);
        if (CollectionUtils.isEmpty(userLikedPostId)) {
            return new Page<>();
        }
        return postMapper.selectUserLikedWorksByUserId(new Page<>(1,userLikedPostId.size()),userLikedPostId);
    }

    @Override
    public List<Long> batchGetLikedPost(Long userId, List<Long> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return null;
        }
        List<Long> longs = postMapper.selectLikedPostIdsByUserId(userId, postIds);

        return longs;
    }

    @Override
    public PostDO getPostById(Long postId) {
        return postMapper.selectById(postId);
    }

}
