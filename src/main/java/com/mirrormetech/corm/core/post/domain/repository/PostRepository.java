package com.mirrormetech.corm.core.post.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;

import java.util.List;

public interface PostRepository {

    /**
     * 根据发布者的userId更新其作品的发布者昵称
     * @param userId 发布者用户ID
     * @param nickName 作品的昵称
     */
    int updatePosterNickNameByUserId(Long userId, String nickName);

    Boolean isExist(Long postId);

    void createPost(PostDO postDO);

    Page<PostDO> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size);

    Page<Post> getAllPostsByCondition(QueryListDTO queryListDTO, Page page);

    Page<PostDO> selectUserWorksByUserId(Page<Object> page, Long userId);

    Page<PostDO> selectUserLikedWorksByUserId(Page<Object> page, Long userId);

    List<Long> batchGetLikedPost(Long userId, List<Long> postIds);

    PostDO getPostById(Long postId);
}
