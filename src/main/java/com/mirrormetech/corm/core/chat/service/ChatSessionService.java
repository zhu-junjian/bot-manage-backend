package com.mirrormetech.corm.core.chat.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.VO.ChatSessionVO;
import com.mirrormetech.corm.core.chat.domain.dto.CreateSessionDTO;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.dto.msg.BaseMsgDTO;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;

public interface ChatSessionService {

    Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId);

    /**
     * 分页查询 会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    Page<ChatSessionVO> selectSessionByUserId(Page<ChatSessionDO> page, QueryListDTO req);

    void deleteSession(Long sessionId);

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

    void createSession(CreateSessionDTO createSessionDTO);

    void sendMessage(BaseMsgDTO dto);
}
