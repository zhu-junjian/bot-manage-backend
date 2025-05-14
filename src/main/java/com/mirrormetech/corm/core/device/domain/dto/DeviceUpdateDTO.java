package com.mirrormetech.corm.core.device.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class DeviceUpdateDTO {

    private Long id;

    @Size(min = 0, max = 20, message = "设备名长度必须为 0-20 个字符")
    private String name;

    private String avatarUrl;

}
