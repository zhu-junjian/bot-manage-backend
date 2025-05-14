package com.mirrormetech.corm.core.device.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 设备绑定
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class DeviceBindDTO {

    private Long userId;

    /**
     * TODO 固定长度校验
     * 设备SN码
     */
    @Size(min = 1, max = 20, message="设备SN长度不合法")
    private String sn;
}
