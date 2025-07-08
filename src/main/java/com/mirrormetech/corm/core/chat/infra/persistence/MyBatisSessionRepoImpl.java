package com.mirrormetech.corm.core.chat.infra.persistence;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.ChatSessionFactory;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.dto.UpdateSessionCondition;
import com.mirrormetech.corm.core.chat.domain.dto.UpdateSessionDTO;
import com.mirrormetech.corm.core.chat.domain.repository.ChatSessionRepo;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.MessagePersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  聊天会话仓储层
 *  是对领域层 仓储接口的实现
 *  下水道逻辑
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisSessionRepoImpl implements ChatSessionRepo {

    // 消息持久化上下文
    private final MessagePersistenceContext persistenceContext;

    private final ChatSessionMapper chatSessionMapper;

    private final ChatSessionFactory chatSessionFactory;

    /**
     * 分页查询会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    @Override
    public Page<ChatSessionDO> selectSessionsByUserId(Page<ChatSessionDO> page, QueryListDTO req) {
        return chatSessionMapper.selectSessionsByUserId(page, req);
    }

    /**
     * 置顶会话
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    @Override
    public int topSession(Long sessionId) {
        return chatSessionMapper.topSession(sessionId);
    }

    /**
     * 取消置顶会话
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    @Override
    public int unTopSession(Long sessionId) {
        return chatSessionMapper.UnTopSession(sessionId);
    }

    /**
     * 创建会话 or 更新会话的update_time
     * 会话列表排序 先按照是否置顶 再按照置顶时间  再按照更新时间
     * 此方法只在消息域调用时使用
     */
    @Override
    public void createOrUpdateSession(Long initiator, Long recipient, Integer sessionType) {
        // 先判断会话是否存在
        String sessionId = switch (sessionType) {
            case 0 -> Math.min(initiator, recipient) + "_" + Math.max(recipient, initiator);
            case 1 -> Math.min(initiator, recipient) + "+" + Math.max(recipient, initiator);
            default -> null;
        };

        boolean existsSession = existsSession(sessionId);
        //如果不存在则创建一条会话记录
        if (!existsSession) {
            // TODO 添加会话类型 sessionType 0 人与人 1  人与设备 @see SessionType
            boolean created = createSession(initiator, recipient, sessionType);
            log.info("ChatSessionRepo createOrUpdateSession initiator :{}, recipient:{}, sessionType:{}, created:{}, ", initiator, recipient, sessionType, created);
        }else {
            boolean updated = updateSession(initiator, recipient, sessionType);
            log.info("ChatSessionRepo createOrUpdateSession initiator :{}, recipient:{} , sessionType:{}, updated:{}", initiator, recipient, sessionType, updated);
        }
        // 更新会话的update时间 版本号  lastmsgid lastmsg content
    }

    /**
     * 发送消息时 更新会话
     * 发送消息场景，应该先调用createOrUpdateSession 然后在调用此方法
     * 业务场景总是：先打开聊天窗，然后再发送消息
     * @param initiator 聊天发起者
     * @param recipient 聊天接收者
     * @param message   发送的消息
     */
    @Override
    @Transactional // TODO 之前有个地方 Transactional的其他方式
    public void updateSessionWithSendMessage(Long initiator, Long recipient, Message message) {
        // 1.更新会话的msg部分 以及 未读数 (TODO 待确认 之前思考 未读数不在session表中冗余)
        /**
         * 1.先插入message数据
         * 2.更新会话msg部分
         */
        String sessionId = null;
        Integer receiverType = message.getReceiverType();
        sessionId = switch (receiverType) {
            case 0 -> Math.min(initiator, recipient) + "_" + Math.max(recipient, initiator);
            case 1 -> Math.min(initiator, recipient) + "+" + Math.max(recipient, initiator);
            default -> sessionId;
        };
        ChatMessageDO chatMessageDO = persistenceContext.processMessage(message);
        Long id = chatMessageDO.getId();
        Integer messageType = message.getMessageType();
        String content = message.getContent();
        if(message.getMessageType().equals(MessageTypeEnum.HAND_MSG.getCode())){
            content = "手信内容";
        }
        Integer version = chatSessionMapper.selectVersionById(sessionId);
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO(id, content, messageType, chatMessageDO.getPersistTime());
        UpdateSessionCondition updateSessionCondition = new UpdateSessionCondition(version, sessionId);
        int i = chatSessionMapper.updateWithNewMessage(updateSessionDTO, updateSessionCondition);
    }

    /**
     * 删除聊天
     * 逻辑删除
     * 在下次发起聊天时 修改状态
     */
    @Override
    public void deleteSession(Long sessionId) {
        chatSessionMapper.deleteSession(sessionId);
    }

    /**
     * 更新会话，在会话被打开时调用
     * @param initiator 聊天发起者
     * @param recipient 聊天接收者
     */
    public boolean updateSession(Long initiator, Long recipient, Integer sessionType) {
        String sessionId = Math.min(initiator, recipient)+"_"+Math.max(recipient, initiator);
        Integer version = chatSessionMapper.selectVersionById(sessionId);
        int i = chatSessionMapper.updateChatSession(sessionId, version);
        return i == 1;
    }

    /**
     * 创建会话记录
     * @param initiator 聊天发起者
     * @param recipient 聊天接收者
     * @return 创建是否成功
     */
    @Override
    public boolean createSession(Long initiator, Long recipient, Integer sessionType){
        ChatSessionDO chatSessionDO = chatSessionFactory.createDO(initiator, recipient, sessionType);
        return chatSessionMapper.insert(chatSessionDO) > 0;
    }

    /**
     * 会话是否存在
     * @param sessionId 会话ID
     * @return 存在or not
     */
    @Override
    public boolean existsSession(String sessionId) {
        return chatSessionMapper.selectBySessionId(sessionId) != null;
    }
}
