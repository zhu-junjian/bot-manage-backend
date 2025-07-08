package com.mirrormetech.corm.core.message.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.mqtt.MqttClientManager;
import com.mirrormetech.corm.core.chat.domain.repository.ChatSessionRepo;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.UnreadMsgDTO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.MessagePersistenceContext;
import com.mirrormetech.corm.core.message.infra.persistence.MyBatisMessageRepoImpl;
import com.mirrormetech.corm.core.user.domain.FollowRelation;
import com.mirrormetech.corm.core.user.domain.dto.UserFollowDTO;
import com.mirrormetech.corm.core.user.domain.repository.UserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息领域服务
 * 和消息领域模型有关的场景 以及 关联其他领域模型场景的能力编排
 * 例如：创建并发送一条消息时，需要同步更新 chat域 chat对象的未读数
 *      查询某个会话的消息时，需要同步更新chat域 chat对象的未读数置为0
 */
@Service
@RequiredArgsConstructor
public class MessageDomainService {

    @Qualifier("myBatisSessionRepoImpl")
    private final ChatSessionRepo chatSessionRepo;

    private final MyBatisMessageRepoImpl myBatisMessageRepo;

    private final MqttClientManager mqttClientManager;

    // 应该关联 关注域的领域服务而不是具体实现
    @Qualifier("myBatisFollowRepoImpl")
    private final UserFollowRepository userFollowRepository;

    private final MessagePersistenceContext messagePersistenceContext;

    /**
     * 已读状态修改，批量修改会话消息的已读状态
     * @param chatSessionId 会话ID
     * @param msgIds 已读的消息ID
     * @return 影响行数
     */
    public int updateRead(Long chatSessionId, Long[] msgIds) {
        return myBatisMessageRepo.updateRead(chatSessionId, msgIds);
    }

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    public Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId){
        return myBatisMessageRepo.selectChatMessageByChatId(page, chatSessionId);
    }

    /**
     * 某个具体聊天会话的未读消息 因为点赞和关注消息的chat_session_id=0 所以这里不需要对msg_type进行过滤
     * @param id 会话表的主键ID
     * @return 此会话未读消息数
     */
    public int getChatUnreadCountById(Long id, Long receiverId){
        return myBatisMessageRepo.getChatUnreadCountById(id, receiverId);
    }

    /**
     * 根据消息ID修改设备接收状态
     * @param msgId 消息ID
     * @param status 设备接收状态
     * @return 影响行数 >0 修改成功
     */
    public int updateDeviceReceiveStatus(Long msgId, Integer status) {
        return myBatisMessageRepo.updateDeviceReceiveStatus(msgId, status);
    }

    /**
     * 根据用户ID查询其被赞消息列表 支持分页
     * TODO cm.* 使用覆盖索引
     * @param page 分页查询条件
     * @param receiverId 被赞者用户ID
     * @param msgType 消息类型 5-点赞 6-关注
     * @return 分页数据
     */
    public Page<LikeInfoDTO> selectLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId){
        Page<LikeInfoDTO> likeInfoDTOPage = myBatisMessageRepo.selectLikeInfo(page, receiverId, MessageTypeEnum.LIKE_MSG.getCode());
        // 2. 批量获取关系状态
        List<Long> targetUserIds = likeInfoDTOPage.getRecords().stream()
                .map(LikeInfoDTO::getSenderId)
                .collect(Collectors.toList());

        Map<Long, FollowRelation> relations = userFollowRepository.batchGetRelations(receiverId, targetUserIds);

        // 3. 组合结果
        List<LikeInfoDTO> resultList = likeInfoDTOPage.getRecords().stream()
                //TODO Map Or Peek
                .peek(user -> {
                    FollowRelation relation = relations.getOrDefault(user.getSenderId(), FollowRelation.NO_RELATION);
                    user.setRelation(relation.getCode());
                })
                .collect(Collectors.toList());

        // 4. 返回分页结果
        Page<LikeInfoDTO> pageList = new Page<>(page.getCurrent(), page.getSize(), likeInfoDTOPage.getTotal());
        pageList.setRecords(resultList);
        return pageList;
    }

    /**
     * 根据用户ID查询其被关注消息列表 支持分页
     * TODO cm.* 使用覆盖索引
     * @param page 分页查询条件
     * @param receiverId 被赞者用户ID
     * @param msgType 消息类型 5-点赞 6-关注
     * @return 分页数据
     */
    public Page<LikeInfoDTO> selectFollowInfo(Page<List<LikeInfoDTO>> page, Long receiverId){
        Page<LikeInfoDTO> likeInfoDTOPage = myBatisMessageRepo.selectLikeInfo(page, receiverId, MessageTypeEnum.FOLLOWING_MSG.getCode());

        // 2. 批量获取关系状态
        List<Long> targetUserIds = likeInfoDTOPage.getRecords().stream()
                .map(LikeInfoDTO::getSenderId)
                .collect(Collectors.toList());

        Map<Long, FollowRelation> relations = userFollowRepository.batchGetRelations(receiverId, targetUserIds);

        // 3. 组合结果
        List<LikeInfoDTO> resultList = likeInfoDTOPage.getRecords().stream()
                //TODO Map Or Peek
                .peek(user -> {
                    FollowRelation relation = relations.getOrDefault(user.getSenderId(), FollowRelation.NO_RELATION);
                    user.setRelation(relation.getCode());
                })
                .collect(Collectors.toList());

        // 4. 返回分页结果
        Page<LikeInfoDTO> pageList = new Page<>(page.getCurrent(), page.getSize(), likeInfoDTOPage.getTotal());
        pageList.setRecords(resultList);
        return pageList;
    }

    /**
     * 获取所有未读信息
     * @param userId 被查询用户ID
     * @return 未读信息列表
     */
    public UnreadMsgDTO getUnreadInfo(Long userId){
        UnreadMsgDTO unreadMsgDTO = new UnreadMsgDTO();
        unreadMsgDTO.setAllUnread(myBatisMessageRepo.getAllUnreadCount(userId));
        unreadMsgDTO.setUnreadLikes(myBatisMessageRepo.getUnreadLikeCount(userId));
        unreadMsgDTO.setUnreadFollowings(myBatisMessageRepo.getUnreadFollowCount(userId));
        unreadMsgDTO.setUnreadMessages(myBatisMessageRepo.getUnreadMsgCount(userId));
        unreadMsgDTO.setSessionUnread(myBatisMessageRepo.getUnreadMsgCountWithSessionId(userId));
        return unreadMsgDTO;
    }

    /**
     * 单纯开启会话
     * @param initiator 聊天发起者
     * @param recipient 聊天接收者
     */
    public void createSession(Long initiator, Long recipient, Integer sessionType){
        chatSessionRepo.createSession(initiator, recipient, sessionType);
    }

    /**
     * TODO 先实现 后考虑 事务失效场景
     * 通过messageFactory创建一个消息对象
     * 在此领域服务内包含：
     *  1.消息域的持久化 事务管理 不同的消息由不同的持久化实现 此处为likeMessagePersistence
     *  2.会话域 对应会话 增加未读消息
     *  3.调用仓储层的接口，发送IM消息，此处的实现为mqtt（mqttPublishService）
     *   3.1 调用仓储层的接口，发送publish 推送消息，此处的实现为 友盟Service
     */
    @Transactional
    public void createAndSend(Message message) {
        //TODO message send  (persistence cache publish mqtt)
        //根据消息类型 从策略模式中获取相应类型的持久化接口
        // 补充消息的megSeqNo
        Long initiator = message.getSenderId();
        Long recipient = message.getReceiverId();
        chatSessionRepo.createOrUpdateSession(initiator, recipient, message.getReceiverType());
        chatSessionRepo.updateSessionWithSendMessage(initiator, recipient, message);
        //messagePersistenceContext.processMessage(message);
        //根据消息类型 从策略模式中获取相应的publish 主题
        try {
            mqttClientManager.publish(message.topic(), JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8), 2);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        //根据消息类型 从策略模式中获取相
    }
}