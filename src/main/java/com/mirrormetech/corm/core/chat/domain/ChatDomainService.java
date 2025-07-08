package com.mirrormetech.corm.core.chat.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.chat.domain.VO.ChatSessionVO;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.repository.ChatSessionRepo;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageDomainService;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.user.domain.UserDomainService;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.infra.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天会话领域服务
 * 领域服务是对 单一或者跨领域模型的  能力编排
 * 点赞 模型 和 关注模型是一种特殊的 chat领域模型 通过 type划分
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatDomainService {

    @Qualifier("myBatisSessionRepoImpl")
    private final ChatSessionRepo mybatisRepo;

    private final UserDomainService userDomainService;

    private final ChatSessionFactory chatSessionFactory;

    private final MessageDomainService messageDomainService;

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    public Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId){
        return messageDomainService.selectChatMessageByChatId(page, chatSessionId);
    }

    /**
     * 分页查询 会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    public Page<ChatSessionVO> selectSessionByUserId(Page<ChatSessionDO> page, QueryListDTO req){
        /*
            1.查询出所有对话框
            2.补充聊天对象的头像信息
            3.补充每个对话框的未读数
         */
        // TODO 如果聊天对象不存在的话 使用匿名ID或者匿名状态
        Page<ChatSessionDO> chatSessionDOPage = mybatisRepo.selectSessionsByUserId(page, req);
        List<ChatSessionDO> records = chatSessionDOPage.getRecords();
        Long userId = req.getUserId();
        Page<ChatSessionVO> chatSessionVOPage = new Page<>();
        List<ChatSessionVO> chatSessionVOS = addTargetUserInfo(records, userId);
        chatSessionVOPage.setRecords(chatSessionVOS);
        chatSessionVOPage.setCurrent(chatSessionDOPage.getCurrent());
        chatSessionVOPage.setSize(chatSessionDOPage.getSize());
        chatSessionVOPage.setTotal(chatSessionDOPage.getTotal());
        return chatSessionVOPage;
    }

    /**
     * 对每个会话 补充 对方的用户信息
     * @param chatSessionDOList 会话列表
     * @param userId 当前查询的用户
     */
    public List<ChatSessionVO> addTargetUserInfo(List<ChatSessionDO> chatSessionDOList, Long userId) {
        if (chatSessionDOList == null || chatSessionDOList.isEmpty()) {
            return null;
        }
        List<ChatSessionVO> voList = new ArrayList<>();
        chatSessionDOList.forEach(chatSessionDO -> {
            /*
             1.补充聊天对方的用户信息
             */
            Long targetUserId;
            Long currentChatSessionId = chatSessionDO.getId();
            if (chatSessionDO.getUser1Id().equals(userId)) {
                targetUserId = chatSessionDO.getUser2Id();
            }else {
                targetUserId = chatSessionDO.getUser1Id();
            }
            UserVO targetUser = userDomainService.getUserById(targetUserId);
            ChatSessionVO chatSessionVO = chatSessionFactory.DOToVO(chatSessionDO);
            if (targetUser != null) {
                chatSessionVO.setTargetUserId(targetUserId);
                chatSessionVO.setNickName(targetUser.getNickName());
                chatSessionVO.setTargetUserAvatarUrl(targetUser.getAvatarUrl());
            }else {
                log.warn("chat domain add user info warn ,userId:{} not exists", targetUserId);
            }
            // 查询单个会话的未读数 TODO 需要判断为 接收方
            chatSessionVO.setSessionUnreadCount(messageDomainService.getChatUnreadCountById(currentChatSessionId, userId));
            voList.add(chatSessionVO);
        });
        return voList;
    }

    /**
     * 删除会话
     * @param sessionId 会话Id
     */
    public void deleteSession(Long sessionId) {
        mybatisRepo.deleteSession(sessionId);
    }

    /**
     * 置顶会话
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    public int topSession(Long sessionId){
        return mybatisRepo.topSession(sessionId);
    }

    /**
     * 取消置顶
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    public int unTopSession(Long sessionId){
        return mybatisRepo.unTopSession(sessionId);
    }

    /**
     * 创建聊天会话记录
     * @param initiator 聊天发起者
     * @param recipient 聊天接收者
     */
    public void createSession(Long initiator, Long recipient, Integer sessionType){
        //真正的聊天中发起方和接收方 角色没有那么重要  只有相对于消息而言是自己的消息 和 对方的消息的区别
        mybatisRepo.createOrUpdateSession(initiator, recipient, sessionType);
    }

    /**
     *
     * @param senderId    发送者ID 目前可能有的场景是：黑小豹官方 系统通知 App用户（User）
     * @param receiverId  接收方ID 目前可能有的场景是：App用户（User) 设备（tb_device.id)
     * @param messages     发送内容  可以支持批量发送
     */
    public void createOrSendChat(Long senderId, Long receiverId, List<Message> messages) {
        /*
            1.先做参数校验  包括合法性 是否存在性  message的valid是否合法等
            2.调用chat域的create 触发 create相关的逻辑 包括持久化数据 更新时间 缓存未读数置为0
            3.调用message域的create 触发create相关逻辑 包括持久化 缓存数据写入
            4.其他领域事件
         */
    }

    /**
     * 查询某个用户的会话列表
     * 支持分页查询
     * @param userId 用户ID
     */
    public void chatList(Long userId) {

    }

    /**
     * 置顶某个会话
     * &#064;check  最多置顶5个
     * @param userId 用户Id
     * @param chatId 会话Id
     */
    public void topChat(Long userId, Long chatId) {

    }

}
