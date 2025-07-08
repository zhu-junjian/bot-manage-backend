package com.mirrormetech.corm.core.chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.ChatDomainService;
import com.mirrormetech.corm.core.chat.domain.VO.ChatSessionVO;
import com.mirrormetech.corm.core.chat.domain.dto.CreateSessionDTO;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.dto.msg.BaseMsgDTO;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.service.ChatSessionService;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageDomainService;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.dto.UnreadMsgDTO;
import com.mirrormetech.corm.core.message.domain.factory.MessageFactoryContext;
import com.mirrormetech.corm.core.message.domain.message.HandMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatDomainService chatDomainService;

    private final MessageDomainService messageDomainService;

    private final MessageFactoryContext messageFactoryContext;

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    @Override
    public Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId) {
        return chatDomainService.selectChatMessageByChatId(page, chatSessionId);
    }

    /**
     * 分页查询 会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    @Override
    public Page<ChatSessionVO> selectSessionByUserId(Page<ChatSessionDO> page, QueryListDTO req) {
        return chatDomainService.selectSessionByUserId(page, req);
    }

    /**
     * 删除会话
     * @param sessionId 会话Id
     */
    @Override
    public void deleteSession(Long sessionId) {
        chatDomainService.deleteSession(sessionId);
    }

    @Override
    public int topSession(Long sessionId) {
        return chatDomainService.topSession(sessionId);
    }

    @Override
    public int unTopSession(Long sessionId) {
        return chatDomainService.unTopSession(sessionId);
    }

    @Override
    public void createSession(CreateSessionDTO createSessionDTO) {
        messageDomainService.createSession(createSessionDTO.getInitiator(), createSessionDTO.getRecipient(), createSessionDTO.getSessionType());
    }

    @Override
    public void sendMessage(BaseMsgDTO dto) {
        // 将
        if(dto.getMessageType().equals(MessageTypeEnum.HAND_MSG.getCode())){
            List<Message> messages = messageFactoryContext.getMessageFactory(dto.getMessageType()).DTOToMessageList(dto);
            messages.forEach(messageDomainService::createAndSend);
        }else {
            Message message = messageFactoryContext.getMessageFactory(dto.getMessageType()).DTOToMessage(dto);
            messageDomainService.createAndSend(message);
        }
    }

    /**
     * 获取某用户ID所有未读信息
     * @param userId 被查询用户ID
     * @return 所有未读信息
     */
    public UnreadMsgDTO getAllUnreadInfo(Long userId) {
        return messageDomainService.getUnreadInfo(userId);
    }
}
