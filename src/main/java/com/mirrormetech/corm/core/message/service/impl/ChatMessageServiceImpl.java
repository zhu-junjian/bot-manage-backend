package com.mirrormetech.corm.core.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.message.domain.MessageDomainService;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.UnreadMsgDTO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final MessageDomainService messageDomainService;

    /**
     * 分页查询会话的历史消息
     * @param page 分页条件
     * @param chatSessionId 会话Id
     * @return 历史消息记录
     */
    @Override
    public Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId) {
        /*
         1.查询历史聊天记录
         2.更新对应记录的已读状态 TODO 注意：只更新当自己为接收者时的消息
         */
        Page<ChatMessageDO> chatMessageDOPage = messageDomainService.selectChatMessageByChatId(page, chatSessionId);
        List<ChatMessageDO> records = chatMessageDOPage.getRecords();
        Long[] ids =records.stream().map(ChatMessageDO::getId).toList().toArray(Long[]::new);
        int i = messageDomainService.updateRead(chatSessionId, ids);
        log.info("update msg read size:{}, updated:{}",ids.length, i);
        return chatMessageDOPage;
    }

    public int updateDeviceReceiveStatus(Long msgId, Integer status) {
        return messageDomainService.updateDeviceReceiveStatus(msgId, status);
    }

    @Override
    public UnreadMsgDTO getUnreadInfo(Long userId) {
        return messageDomainService.getUnreadInfo(userId);
    }

    public Page<LikeInfoDTO> getLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId){
        return messageDomainService.selectLikeInfo(page, receiverId);
    }

    public Page<LikeInfoDTO> getFollowInfo(Page<List<LikeInfoDTO>> page, Long receiverId){
        return messageDomainService.selectFollowInfo(page, receiverId);
    }
}
