package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

/**
 * 关注消息的内容
 */
@Data
public class FollowingContent implements Content {

    //private Integer fresh;

    @Override
    public Integer messageType() {
        return MessageTypeEnum.FOLLOWING_MSG.getCode();
    }
}
