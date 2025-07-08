package com.mirrormetech.corm.core.post.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.security.SecurityContextUtil;
import com.mirrormetech.corm.core.category.domain.FirstLevelCategoryFactory;
import com.mirrormetech.corm.core.category.domain.SecondLevelCategoryFactory;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import com.mirrormetech.corm.core.category.infra.persistence.MyBatisFirstLevelCategoryRepository;
import com.mirrormetech.corm.core.category.infra.persistence.MyBatisSecondLevelCategoryRepository;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.domain.dto.WorksQueryDTO;
import com.mirrormetech.corm.core.post.domain.repository.PostRepository;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import com.mirrormetech.corm.core.topic.domain.PostTopic;
import com.mirrormetech.corm.core.topic.domain.PostTopicFactory;
import com.mirrormetech.corm.core.topic.domain.Topic;
import com.mirrormetech.corm.core.topic.domain.repository.PostTopicRepository;
import com.mirrormetech.corm.core.topic.domain.repository.TopicRepository;
import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;
import com.mirrormetech.corm.core.user.domain.repository.UserRepository;
import com.mirrormetech.corm.core.user.infra.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容发布 领域服务
 *
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

    @Qualifier("myBatisPostRepository")
    private final PostRepository postRepository;

    @Qualifier("myBatisUserRepoImpl")
    private final UserRepository userRepository;

    /**
     * 根据发布者的userId更新其作品的发布者昵称
     * @param userId 发布者用户ID
     * @param nickName 作品的昵称
     */
    public int updatePosterNickNameByUserId(Long userId, String nickName) {
        return postRepository.updatePosterNickNameByUserId(userId, nickName);
    }

    /**
     * 查询目标用户喜欢作品列表 包含点赞数量，同时标记源用户对其作品的点赞关系
     * @param worksQueryDTO 源用户ID 目标用户ID  page参数
     * @return 作品列表- 分页
     */
    public Page<PostDO> getUserLikeWorksByUserId(WorksQueryDTO worksQueryDTO) {
        /*
           1.参数校验
           2.根据targetUserId查询 目标用户喜欢的作品集合
           3.根据sourceUserId 查询用户对当前页作品的点赞情况
         */
        Long sourceUserId = worksQueryDTO.getSourceUserId() == null ? securityContextUtil.getCurrentUserId() : worksQueryDTO.getSourceUserId();
        Long targetUserId = worksQueryDTO.getTargetUserId();
        if(!userRepository.userExists(sourceUserId) || !userRepository.userExists(targetUserId)){
            throw new ServiceException(ExceptionCode.QUERY_ERROR_USER_NOT_EXIST);
        }
        Integer page = worksQueryDTO.getPage() == null ? 1 : worksQueryDTO.getPage();
        Integer size = worksQueryDTO.getSize() == null ? 10 : worksQueryDTO.getSize();
        Page<PostDO> listPage = myBatisPostRepository.selectUserLikedWorksByUserId(new Page<>(page, size), targetUserId);
        listPage = markUserLike(listPage, sourceUserId);
        return listPage;
    }


    /**
     * 查询用户作品，按照时间倒序排序- 分页
     * @param worksQueryDTO 源用户ID 目标用户ID 分页参数
     * @return 其作品集合-分页
     */
    public Page<PostDO> getUserWorksByUserId(WorksQueryDTO worksQueryDTO) {
        /*
           1.参数校验
           2.根据targetUserId查询 目标用户作品集合
           3.根据sourceUserId 查询用户对当前页作品的点赞情况
         */
        Long sourceUserId = worksQueryDTO.getSourceUserId() == null ? securityContextUtil.getCurrentUserId() : worksQueryDTO.getSourceUserId();
        Long targetUserId = worksQueryDTO.getTargetUserId();
        Integer page = worksQueryDTO.getPage() == null ? 1 : worksQueryDTO.getPage();
        Integer size = worksQueryDTO.getSize() == null ? 10 : worksQueryDTO.getSize();
        if(!userRepository.userExists(sourceUserId) || !userRepository.userExists(targetUserId)){
            throw new ServiceException(ExceptionCode.QUERY_ERROR_USER_NOT_EXIST);
        }
        Page<PostDO> listPage = myBatisPostRepository.selectUserWorksByUserId(new Page<>(page, size), targetUserId);
        listPage = markUserLike(listPage, sourceUserId);
        return listPage;
    }

    /**
     * 标记当前用户对目标集合中内容的点赞状态
     *
     * @param listPage 当前集合
     * @param userId   源用户ID
     * @return 对当前集合标记源用户的点赞状态
     */
    public Page<PostDO> markUserLike(Page<PostDO> listPage, Long userId) {
        // 根据sourceUserId查询当前用户对此页作品的点赞情况
        List<Long> postIds = listPage.getRecords().stream().
                map(PostDO::getId).toList();

        List<Long> likedPost = postRepository.batchGetLikedPost(userId, postIds);

        List<PostDO> resultList = listPage.getRecords().stream().map(postDO -> {
            postDO.setIsLiked(likedPost.contains(postDO.getId()));
            return postDO;
        }).collect(Collectors.toList());

        listPage.setRecords(resultList);
        return listPage;
    }

    /**
     * 分页查询
     * &#064;Deprecated
     * @see PostDomainService#getAllPosts
     * @param queryListDTO 查询条件
     * @param pageNum      页码
     * @param size         一页条数
     * @return 列表集合
     */
    public Page<Post> getAllPostsByCondition(QueryListDTO queryListDTO, Integer pageNum, Integer size) {
        //添加过滤条件 1. 必须为精选  2.状态为正常状态
        queryListDTO.setIsFeatured(FeaturedStatus.FEATURED.getCode());
        queryListDTO.setStatus(PostStatus.NORMAL.getCode());
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
     * 对如下方法的修改，不拷贝属性直接返回postList
     * @see PostDomainService#getAllPostsByCondition(QueryListDTO, Integer, Integer)
     */
    public Page<Post> getAllPosts(QueryListDTO queryListDTO, Page page) {
        //添加过滤条件 1. 必须为精选  2.状态为正常状态
        queryListDTO.setIsFeatured(FeaturedStatus.FEATURED.getCode());
        queryListDTO.setStatus(PostStatus.NORMAL.getCode());
        return myBatisPostRepository.getAllPostsByCondition(queryListDTO, page);
    }

    /**
     * 为发布内容补充用户基本信息
     *
     * @param post current context
     */
    public void findUserInfo(Post post) {
        User currentUser = securityContextUtil.getCurrentUser();
        post.setUsername(securityContextUtil.getCurrentUserDetail().username);
        post.setNickName(securityContextUtil.getCurrentUserDetail().nickName);
        post.setAvatarUrl(securityContextUtil.getCurrentUserDetail().avatarUrl);
    }

    /**
     * 1.postFactory 创建post对象
     * 2.映射DO DO入库
     * 3.补充话题、大类信息
     * 4.返回
     * 插入postDO & post_topic
     *
     * @param postDTO
     */
    //@Transactional
    public Post createPost(PostDTO postDTO) {
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
     *
     * @param posts 内容列表
     */
    public void findUserInfoForList(List<Post> posts) {
        posts.forEach(this::findUserInfo);
    }

    /**
     * 补充用户 内容列表点赞信息
     *
     * @param userId 用户Id
     * @param post   内容
     * @return 是否点赞
     */
    public boolean userLikedPost(Long userId, Post post) {
        Long postId = post.getId();
        QueryWrapper<PostLikeDO> queryWrapper = new QueryWrapper<PostLikeDO>().eq("user_id", userId).eq("post_id", postId).eq("status", 0);
        List<PostLikeDO> postLikes = postLikeRepository.selectList(queryWrapper);
        return !postLikes.isEmpty();
    }

    /**
     * 查询 单条内容的点赞数量
     *
     * @param postList 内容列表
     */
    public void getPostLikeList(List<Post> postList) {
        postList.forEach(this::getPostLike);
    }

    /**
     * 查询 单条内容的点赞数量
     *
     * @param post 单条内容
     */
    public void getPostLike(Post post) {
        Long postId = post.getId();
        QueryWrapper<PostLikeDO> queryWrapper = new QueryWrapper<PostLikeDO>().eq("post_id", postId).eq("status", 0);
        List<PostLikeDO> postLikeList = postLikeRepository.selectList(queryWrapper);
        post.setLikeCount(postLikeList.size());
    }

    public void userLikedForList(Long userId, List<Post> postList) {
        postList.forEach(post -> {
            post.setLiked(userLikedPost(userId, post));
        });
    }

    /**
     * 为内容列表补充话题信息
     *
     * @param postList 内容列表
     */
    public void findTopicForList(List<Post> postList) {
        postList.forEach(this::findTopic);
    }

    /**
     * 单个post增加话题数量限制
     *
     * @param post 单个发布内容
     */
    public void findTopic(Post post) {
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
     * @param post  单条发布内容
     * @param flcId 一级大类Id
     * @param slcId 二级大类Id
     */
    public void findCategory(Post post, Long flcId, Long slcId) {
        FirstLevelCategoryDO firstLevelCategoryDO;
        SecondLevelCategoryDO secondLevelCategoryDO;
        if (flcId == null) {
            return;
        }
        firstLevelCategoryDO = flcRepository.selectById(flcId);
        if (slcId == null) {
            secondLevelCategoryDO = new SecondLevelCategoryDO();
        } else {
            secondLevelCategoryDO = slcRepository.selectById(slcId);
        }
        //TODO 修改当ficId不为空，但是数据库没有相应id对应的内容时产生的bug
        if (firstLevelCategoryDO == null) {
            post.setFirstLevelCategoryInfo(null);
        } else {
            post.setFirstLevelCategoryInfo(firstLevelCategoryFactory.create(firstLevelCategoryDO));
        }
        if (secondLevelCategoryDO == null) {
            post.setSecondLevelCategoryInfo(null);
        } else {
            post.setSecondLevelCategoryInfo(secondLevelCategoryFactory.create(secondLevelCategoryDO));
        }
    }

    /**
     * @param postList 内容列表
     */
    public void findCategoryForList(List<Post> postList) {
        postList.forEach(post -> {
            findCategory(post, post.getFirstLevelCategory(), post.getSecondLevelCategory());
        });
    }

}
