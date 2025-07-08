package com.mirrormetech.corm.core.chat.domain.VO;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话展示内容
 */
@Data
public class ChatSessionVO {

    /**
     * 聊天会话列表补充信息
     * 聊天对象的Id
     * 聊天对象的昵称/设备名
     * 聊天对象的头像
     */
    private Long targetUserId;

    private String nickName;

    private String targetUserAvatarUrl;

    private Integer sessionUnreadCount;

    // 会话的主键ID
    private Long id;

    /**
     * 会话ID min(user1_id,user2_id)_max(user1_id,user2_id)组成
     * @see com.mirrormetech.corm.core.chat.domain.SessionTypeEnum
     * 当人与设备通信时 min(user1_id,user2_id)+max(user1_id,user2_id)组成
     */
    private String sessionId;

    // 最后一条消息的ID
    private Long lastMsgId;

    //最后一条消息的摘要 便于展示
    private String lastMsgContent;

    // 最后一条消息的类型
    private Integer lastMsgType;

    // 是否置顶 0-否 1-是
    private Integer isTop;

    // 置顶时间
    private LocalDateTime topTime;

    // 置顶顺序 0-未置顶
    //private Integer topOrder;

    // 聊天的更新时间 最近一次聊天时间
    private LocalDateTime updateTime;

    // 聊天的创建时间
    private LocalDateTime createTime;
}
