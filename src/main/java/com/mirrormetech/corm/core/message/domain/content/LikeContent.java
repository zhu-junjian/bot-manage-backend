package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeContent implements Content {

    @Override
    public Integer messageType() {
        return MessageTypeEnum.LIKE_MSG.getCode();
    }
}
