package com.mirrormetech.corm.core.chat.infra.DO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天会话表
 * chat_session
 */
@Data
@TableName("chat_session")
public class ChatSessionDO {

    // 会话的主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID min(user1_id,user2_id)_max(user1_id,user2_id)组成
     * @see com.mirrormetech.corm.core.chat.domain.SessionTypeEnum
     * 当人与设备通信时 min(user1_id,user2_id)+max(user1_id,user2_id)组成
     */
    @TableField("session_id")
    private String sessionId;

    // 聊天
    @TableField("user1_id")
    private Long user1Id;

    //
    @TableField("user2_id")
    private Long user2Id;

    // 最后一条消息的ID
    @TableField("last_msg_id")
    private Long lastMsgId;

    //最后一条消息的摘要 便于展示
    @TableField("last_msg_content")
    private String lastMsgContent;

    // 最后一条消息的类型
    @TableField("last_msg_type")
    private Integer lastMsgType;

    // user1的未读
    @TableField("unread_count1")
    private Integer unreadCount1;

    // user2的未读
    @TableField("unread_count2")
    private Integer unreadCount2;

    // 是否置顶 0-否 1-是
    @TableField("is_top")
    private Integer isTop;

    // 置顶时间
    @TableField("top_time")
    private LocalDateTime topTime;

    // 置顶顺序 0-未置顶
    @TableField("top_order")
    private Integer topOrder;

    // 乐观锁
    @Version
    private Integer version;

    // 聊天的更新时间 最近一次聊天时间
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;

    // 聊天的创建时间
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
}
