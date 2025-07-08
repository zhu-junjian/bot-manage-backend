package com.mirrormetech.corm.core.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.message.domain.dto.LikeInfoDTO;
import com.mirrormetech.corm.core.message.domain.dto.UnreadMsgDTO;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;

import java.util.List;

public interface ChatMessageService {

    Page<ChatMessageDO> selectChatMessageByChatId(Page<ChatMessageDO> page, Long chatSessionId);

    int updateDeviceReceiveStatus(Long msgId, Integer status);

    UnreadMsgDTO getUnreadInfo(Long userId);

    Page<LikeInfoDTO> getLikeInfo(Page<List<LikeInfoDTO>> page, Long receiverId);

    Page<LikeInfoDTO> getFollowInfo(Page<List<LikeInfoDTO>> page, Long receiverId);
}
