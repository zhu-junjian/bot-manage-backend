package com.mirrormetech.corm.core.message.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.SessionUnreadDTO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;

import java.util.List;

public interface ChatMessageMixedRepo {

    Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId);

    Page<LikeInfoDTO> selectLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId, Integer msgType);

    /**
     * 根据用户ID查询其 所有未读数
     * @param userId 被查询用户
     * @return 未读数
     */
    int getAllUnreadCount(Long userId);

    /**
     * 根据用户ID查询其 未读点赞数
     * @param userId 被查询用户
     * @return 未读点赞数
     */
    int getUnreadLikeCount(Long userId);

    /**
     * 根据用户ID查询其 未读关注数
     * @param userId 被查询用户
     * @return 未读关注数
     */
    int getUnreadFollowCount(Long userId);

    /**
     * 根据用户ID查询其 未读消息总数
     * @param userId 被查询用户
     * @return 未读消息总数
     */
    int getUnreadMsgCount(Long userId);

    /**
     * 根据用户ID查询其 各个会话的未读数
     * @param userId 被查询用户
     * @return 会话&其未读数 集合
     */
    List<SessionUnreadDTO> getUnreadMsgCountWithSessionId(Long userId);

    /**
     * 根据用户ID查询其 具体某个会话的未读数
     * @param userId 用户ID
     * @param sessionId 会话ID ？ 这里是用会话表的ID 还是 会话表的 sessionId
     * @return 此会话的未读数
     */
    int getUnreadMsgCountBySessionId(Long userId, String sessionId);
}
