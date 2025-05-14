package com.mirrormetech.corm.core.device.infra.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
@TableName("tb_robot_device")
public class DeviceDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("device_model")
    private String deviceModel;

    @TableField("name")
    private String name;

    @TableField(value = "serial_num")
    private String serialNum;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("device_ip")
    private String deviceIp;

    @TableField("server_ip")
    private String serverIp;

    @TableField("purchase_date")
    private Date purchaseDate;

    @TableField("warranty_expiry")
    private Date warrantyExpiry;

    @TableField("last_heartbeat")
    private Timestamp lastHeartbeat;

    @TableField("current_status")
    private DeviceStatus currentStatus; // 状态枚举

    @TableField("last_operation_time")
    private Timestamp lastOperationTime;

    @TableField("sensor_info")
    private String sensorInfo;

    /*@TableField(typeHandler = JacksonTypeHandler.class) // JSON字段处理
    private SensorStatus sensorStatus;*/

    @TableField("location")
    private String location; // 存储GeoJSON字符串

    @TableField("battery_level")
    private BigDecimal batteryLevel;

    @TableField("notes")
    private String notes;
}
