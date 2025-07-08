package com.mirrormetech.corm.core.user.domain;

import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageDomainService;
import com.mirrormetech.corm.core.message.domain.factory.FollowingMsgFactory;
import com.mirrormetech.corm.core.user.infra.persistence.MyBatisFollowRepoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * follow域 领域服务
 */
@Service
@RequiredArgsConstructor
public class FollowDomainService {

    private final MyBatisFollowRepoImpl myBatisFollowRepo;

    private final FollowingMsgFactory followingMsgFactory;

    private final MessageDomainService messageDomainService;

    public void followUser(Long followerId, Long followingId){
        myBatisFollowRepo.followUser(followerId, followingId);
        // TODO 发布关注消息
        Message followMessage = followingMsgFactory.createMessage(followerId, followingId, new Timestamp(System.currentTimeMillis()));
        messageDomainService.createAndSend(followMessage);
    }

    /**
     * 根据当前用户查询关注数
     * @param userId 当前用户ID
     * @return 关注数
     */
    public Integer getFollowingsByUserId(Long userId) {
        return myBatisFollowRepo.getFollowingsCount(userId);
    }

    /**
     * 查询当前用户粉丝数
     * @param userId 当前用户ID
     * @return 粉丝数
     */
    public Integer getFollowersByUserId(Long userId){
        return myBatisFollowRepo.getFollowersCount(userId);
    }

}
