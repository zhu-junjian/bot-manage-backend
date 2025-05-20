package com.mirrormetech.corm.core.post.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.security.SecurityContextUtil;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.PostDomainService;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/04/28
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final SecurityContextUtil securityContextUtil;

    private final PostDomainService postDomainService;

    /**
     * @param postDTO
     * @return saved target
     */
    @Override
    public Post create(PostDTO postDTO) {
        return postDomainService.createPost(postDTO);
    }

    /**
     * 通过领域服务关联
     * 按照官方排序 热度
     * @return 分页列表
     */
    @Override
    public Page<Post> getAllPosts(QueryListDTO queryListDTO, Integer pageNum, Integer size) {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        //获取符合当前条件的所有
        Page<Post> pagePostList = postDomainService.getAllPostsByCondition(queryListDTO, pageNum, size);
        List<Post> postList = pagePostList.getRecords();
        //用户内容点赞信息
        postDomainService.userLikedForList(currentUserId, postList);
        //补充话题信息
        postDomainService.findTopicForList(postList);
        //补充大类信息 遍历所有post集合，查询每条记录对应的大类信息
        postDomainService.findCategoryForList(postList);
        //TODO 补充点赞数
        postDomainService.getPostLikeList(postList);
        pagePostList.setRecords(postList);
        return pagePostList;
    }

}
