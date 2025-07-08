package com.mirrormetech.corm.core.chat.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.message.domain.Message;
import org.apache.ibatis.annotations.Param;

/**
 * 聊天会话 持久化能力 接口
 */
public interface ChatSessionRepo {

    /**
     * 分页查询 会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    Page<ChatSessionDO> selectSessionsByUserId(Page<ChatSessionDO> page, @Param("req") QueryListDTO req);

    /**
     * 置顶会话
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    int topSession(Long sessionId);

    /**
     * 取消置顶
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    int unTopSession(Long sessionId);

    void createOrUpdateSession(Long senderId, Long receiverId, Integer sessionType);

    void updateSessionWithSendMessage(Long initiator, Long recipient, Message message);

    /**
     * 删除会话
     * @param sessionId 会话Id
     */
    void deleteSession(Long sessionId);

    boolean createSession(Long initiatorId, Long recipientId, Integer sessionType);

    boolean existsSession(String sessionId);
}
