package com.mirrormetech.corm.core.like.domain;

import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import com.mirrormetech.corm.core.message.domain.factory.LikeMessageFactory;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageDomainService;
import com.mirrormetech.corm.core.post.domain.repository.PostRepository;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import com.mirrormetech.corm.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 用户点赞 - 领域服务
 * @author spencer
 * @date 2025/05/07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeDomainService {

    @Qualifier("myBatisPostLikeRepository")
    private final PostLikeRepository postLikeRepository;

    @Qualifier("myBatisPostRepository")
    private final PostRepository postRepository;

    @Qualifier("myBatisUserRepoImpl")
    private final UserRepository userRepository;

    private final PostLikeFactory factory;

    private final LikeMessageFactory likeMessageFactory;

    private final MessageDomainService messageDomainService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 查询用户作品点赞数
     * @param userId 目标用户
     * @return 点赞数
     */
    public Integer userWorksLikeCount(Long userId) {
        return postLikeRepository.getUserWorksLikeCount(userId);
    }

    /**
     * 点赞或者取消点赞
     * @param postLikeDTO
     * @return
     */
    public String likeOrUnlike(PostLikeDTO postLikeDTO) {
        if (postLikeDTO.getUserId() == null) {
            throw new ServiceException(ExceptionCode.INVALID_PARAM);
        }
        /*
            1.参数校验
            2.点赞
            3.取消点赞
         */
        boolean postExist = postRepository.isExist(postLikeDTO.getPostId());
        boolean userExists = userRepository.userExists(postLikeDTO.getUserId());
        boolean statusValid = LikeStatus.contains(postLikeDTO.getStatus());

        if (!postExist || !userExists || !statusValid) {
            throw new ServiceException(ExceptionCode.INVALID_PARAM);
        }
        int status = postLikeDTO.getStatus();
        return switch (status) {
            case 0 -> {
                likeAction(postLikeDTO);
                yield "点赞成功";
            }
            case 1 -> {
                unlikeAction(postLikeDTO);
                yield "取消点赞成功";
            }
            default -> null;
        };
    }

    /**
     * 点赞
     * @param postLikeDTO status =0
     */
    public void likeAction(PostLikeDTO postLikeDTO){
        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(postLikeDTO);
        /*
          如果 postLikeDOOptional is null  全新点赞
          如果不是 根据情况判断已经点赞 和 存在已取消，重新激活点赞
         */
        // 已存在有效点赞，TODO 抛出已点赞异常
        if (existingLike.isPresent() && existingLike.get().isLikeValid()) {
            throw new ServiceException(ExceptionCode.ALREADY_LIKED);
        }
        postLikeDTO.setStatus(null);
        Optional<PostLike> alreadyLiked = postLikeRepository.findByUserAndPost(postLikeDTO);
        PostLikeDO postLikeDO = factory.getDOByDTO(postLikeDTO);
        postLikeDO.setLikeTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        PostDO post = postRepository.getPostById(postLikeDTO.getPostId());
        if (post == null) {
            log.error("当前点赞的post{}不存在userId！", postLikeDTO.getUserId());
            throw new ServiceException(ExceptionCode.INVALID_PARAM);
        }
        Long userId = post.getUserId();
        if (alreadyLiked.isPresent()) {
            // 存在但已取消（重新激活） TODO 返回点赞成功

            postLikeDO.setId(alreadyLiked.get().getId());
            postLikeDO.setStatus(0);
            postLikeRepository.update(postLikeDO);
        }else {
            // 全新点赞
            postLikeRepository.save(postLikeDO);
        }
        // 创建点赞消息  交给点赞领域服务处理
        Message likeMessage = likeMessageFactory.createMessage(postLikeDTO.getUserId(), userId, new Timestamp(System.currentTimeMillis()));
        //TODO 修改为异步？
        messageDomainService.createAndSend(likeMessage);
        //eventPublisher.publishEvent(new LikeDomainEvent(this,postLikeDTO.getUserId(), userId, new Timestamp(System.currentTimeMillis())));

    }

    /**
     * 取消点赞
     * @param postLikeDTO status =1
     */
    public void unlikeAction(PostLikeDTO postLikeDTO) {
        PostLikeDTO everLiked = new PostLikeDTO();
        everLiked.setPostId(postLikeDTO.getPostId());
        everLiked.setUserId(postLikeDTO.getUserId());
        Optional<PostLike> everLikedOptional = postLikeRepository.findByUserAndPost(everLiked);
        //点过 || 已取消
        if (everLikedOptional.isPresent()) {
            postLikeDTO.setId(everLikedOptional.get().getId());
            // 查询是否点赞
            postLikeDTO.setStatus(0);
            Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(postLikeDTO);
            //已经取消点赞，返回取消失败，异常
            if (existingLike.isEmpty() && everLikedOptional.get().isUnLikeValid()) {
                throw new ServiceException(ExceptionCode.ALREADY_UNLIKED);
            }
            PostLikeDO postLikeDO = factory.getDOByDTO(postLikeDTO);
            // 存在点赞，取消 TODO 返回点赞成功
            if (existingLike.isPresent()) {
                postLikeDO.setStatus(1);
                postLikeDO.setLikeTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
                postLikeRepository.update(postLikeDO);
            }
        }else {
            // 没有点赞信息，取消失败
            throw new ServiceException(ExceptionCode.UNLIKE_ERROR);
        }
    }
}
