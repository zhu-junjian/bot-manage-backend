package com.mirrormetech.corm.core.device.domain.vo;

import com.mirrormetech.corm.core.device.infra.DO.DeviceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class DeviceVO {

    private Long id;

    private String deviceModel;

    private String name;

    private String serialNum;

    private String avatarUrl;

    private String deviceIp;

    private String serverIp;

    private Date purchaseDate;

    private Date warrantyExpiry;

    private Timestamp lastHeartbeat;

    private DeviceStatus currentStatus; // 状态枚举

    private Timestamp lastOperationTime;

    private String sensorInfo;

    /*@TableField(typeHandler = JacksonTypeHandler.class) // JSON字段处理
    private SensorStatus sensorStatus;*/

    /*@TableField("location")
    private String location;*/ // 存储GeoJSON字符串

    private BigDecimal batteryLevel;
}
