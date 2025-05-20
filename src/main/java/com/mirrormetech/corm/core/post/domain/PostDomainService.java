package com.mirrormetech.corm.core.post.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.security.SecurityContextUtil;
import com.mirrormetech.corm.core.category.domain.FirstLevelCategory;
import com.mirrormetech.corm.core.category.domain.FirstLevelCategoryFactory;
import com.mirrormetech.corm.core.category.domain.SecondLevelCategory;
import com.mirrormetech.corm.core.category.domain.SecondLevelCategoryFactory;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.persistence.MyBatisFirstLevelCategoryRepository;
import com.mirrormetech.corm.core.category.infra.persistence.MyBatisSecondLevelCategoryRepository;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.domain.repository.PostRepository;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import com.mirrormetech.corm.core.topic.domain.PostTopic;
import com.mirrormetech.corm.core.topic.domain.PostTopicFactory;
import com.mirrormetech.corm.core.topic.domain.Topic;
import com.mirrormetech.corm.core.topic.domain.repository.PostTopicRepository;
import com.mirrormetech.corm.core.topic.domain.repository.TopicRepository;
import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 内容发布 领域服务
 * @author spencer
 * @date 2025/05/06
 */
@Service
@RequiredArgsConstructor
public class PostDomainService {

    @Qualifier("myBatisPostLikeRepository")
    private final PostLikeRepository postLikeRepository;

    private final PostTopicRepository myBatisPostTopicRepository;

    private final TopicRepository myBatisTopicRepository;

    private final MyBatisFirstLevelCategoryRepository flcRepository;

    private final MyBatisSecondLevelCategoryRepository slcRepository;

    private final SecurityContextUtil securityContextUtil;

    private final PostFactory postFactory;

    private final PostRepository myBatisPostRepository;

    private final PostTopicFactory postTopicFactory;

    private final FirstLevelCategoryFactory firstLevelCategoryFactory;

    private final SecondLevelCategoryFactory secondLevelCategoryFactory;

    /**
     * 分页查询
     * @param queryListDTO 查询条件
     * @param pageNum 页码
     * @param size 一页条数
     * @return 列表集合
     */
    public Page<Post> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size) {
        Page<PostDO> postDoList = myBatisPostRepository.getAllPostsByCondition(queryListDTO, pageNum, size);
        Page<Post> pagePostList = new Page<>();
        List<Post> postList = postDoList.getRecords().stream().map(postDO -> {
            Post post = new Post();
            BeanUtils.copyProperties(postDO, post);
            return post;
        }).toList();
        pagePostList.setRecords(postList);
        pagePostList.setCurrent(postDoList.getCurrent());
        pagePostList.setSize(postDoList.getSize());
        pagePostList.setTotal(postDoList.getTotal());
        pagePostList.setPages(postDoList.getPages());
        return pagePostList;
    }

    /**
     * 为发布内容补充用户基本信息
     * @param post current context
     */
    public void findUserInfo(Post post){
        post.setUsername(securityContextUtil.getCurrentUser().username);
        post.setNickName(securityContextUtil.getCurrentUser().nickName);
        post.setAvatarUrl(securityContextUtil.getCurrentUser().avatarUrl);
    }

    /**
     * 1.postFactory 创建post对象
     * 2.映射DO DO入库
     * 3.补充话题、大类信息
     * 4.返回
     * 插入postDO & post_topic
     * @param postDTO
     */
    //@Transactional
    public Post createPost(PostDTO postDTO){
        // 1.校验参数
        // 传入的是DTO对象，返回的是VO对象，中间过程： 1 将绑定的话题 大类信息入库 2 补充大类信息 和 话题信息进入vo
        //创建postDO对象 post.create post copy
        Post newPost = postFactory.create(postDTO);
        //获取用户名和 用户头像
        findUserInfo(newPost);
        //2.包装实体对象  关联topic 和 first second
        PostDO postDO = postFactory.createDO(newPost);
        //insert do topic infra层的接口 @Transactional
        myBatisPostRepository.createPost(postDO);
        newPost.setId(postDO.getId());
        Long[] topicArr = postDTO.getTopics();
        if (topicArr != null) {
            Arrays.stream(topicArr).forEach(topicId -> {
                myBatisPostTopicRepository.save(new PostTopicDO(postDO.getId(), topicId));
            });
        }
        //补充话题信息
        findTopic(newPost);
        //补充大类信息
        findCategory(newPost, postDTO.getFirstLevelCategory(), postDTO.getSecondLevelCategory());
        return newPost;
    }

    /**
     * 补充用户基本信息
     * @param posts 内容列表
     */
    public void findUserInfoForList(List<Post> posts){
        posts.forEach(this::findUserInfo);
    }

    /**
     * 补充用户 内容列表点赞信息
     * @param userId 用户Id
     * @param post 内容
     * @return 是否点赞
     */
    public boolean userLikedPost(Long userId, Post post) {
        Long postId = post.getId();
        QueryWrapper<PostLikeDO> queryWrapper = new QueryWrapper<PostLikeDO>().eq("user_id",userId).eq("post_id",postId).eq("status",0);
        List<PostLikeDO> postLikes = postLikeRepository.selectList(queryWrapper);
        return !postLikes.isEmpty();
    }

    /**
     * 查询 单条内容的点赞数量
     * @param postList 内容列表
     */
    public void getPostLikeList(List<Post> postList) {
        postList.forEach(this::getPostLike);
    }

    /**
     * 查询 单条内容的点赞数量
     * @param post 单条内容
     */
    public void getPostLike(Post post) {
        Long postId = post.getId();
        QueryWrapper<PostLikeDO> queryWrapper = new QueryWrapper<PostLikeDO>().eq("post_id", postId).eq("status", 0);
        List<PostLikeDO> postLikeList = postLikeRepository.selectList(queryWrapper);
        post.setLikeCount(postLikeList.size());
    }

    public void userLikedForList(Long userId, List<Post> postList) {
        postList.forEach(post -> {post.setLiked(userLikedPost(userId, post)); });
    }

    /**
     * 为内容列表补充话题信息
     * @param postList 内容列表
     */
    public void findTopicForList(List<Post> postList){
        postList.forEach(this::findTopic);
    }

    /**
     * 单个post增加话题数量限制
     * @param post 单个发布内容
     */
    public void findTopic(Post post){
        List<PostTopic> postTopics = myBatisPostTopicRepository.selectList(post.getId());
        List<Topic> topics = new ArrayList<>();
        if (postTopics != null) {
            postTopics.forEach(postTopic -> {
                Long topicId = postTopic.getTopicId();
                topics.addAll(myBatisTopicRepository.selectListById(topicId));
            });
            post.setTopics(topics);
        }
    }

    /**
     * @param post 单条发布内容
     * @param flcId 一级大类Id
     * @param slcId 二级大类Id
     */
    public void findCategory(Post post, Long flcId, Long slcId){
        FirstLevelCategoryDO firstLevelCategoryDO;
        SecondLevelCategoryDO secondLevelCategoryDO;
        if(flcId == null){
            return;
        }
        firstLevelCategoryDO = flcRepository.selectById(flcId);
        if (slcId == null) {
            secondLevelCategoryDO = new SecondLevelCategoryDO();
        }else {
            secondLevelCategoryDO = slcRepository.selectById(slcId);
        }
        post.setFirstLevelCategoryInfo(firstLevelCategoryFactory.create(firstLevelCategoryDO));
        post.setSecondLevelCategoryInfo(secondLevelCategoryFactory.create(secondLevelCategoryDO));
    }

    /**
     * @param postList 内容列表
     */
    public void findCategoryForList(List<Post> postList){
        postList.forEach(post -> {findCategory(post, post.getFirstLevelCategory(), post.getSecondLevelCategory());});
    }

}
