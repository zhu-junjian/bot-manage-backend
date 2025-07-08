package com.mirrormetech.corm.core.message.domain.dto;

import lombok.Data;

@Data
public class UpdateReceiveStatus {

    /**
     * @see com.mirrormetech.corm.core.chat.domain.DeviceReceiveStatus
     */
    private Integer status;

}
