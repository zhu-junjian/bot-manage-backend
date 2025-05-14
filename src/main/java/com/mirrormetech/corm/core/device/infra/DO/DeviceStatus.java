package com.mirrormetech.corm.core.device.infra.DO;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum DeviceStatus {
    // 枚举定义（按业务优先级排序）
    // 添加带启用状态 或者 正常 状态 因为 即便已经被连接过 还会存在 和第三人 绑定的场景
    ONLINE("online", "在线", true),
    OFFLINE("offline", "离线", false),
    MAINTENANCE("maintenance", "维护中", false),
    FAULT("fault", "故障", false),
    SCRAPPED("scrapped", "报废", false);

    // 字段定义
    @EnumValue
    private final String code;        // 数据库存储值
    private final String description; // 用户友好描述
    private final boolean isActive;   // 是否参与业务统计

    // 构造函数（私有化）
    DeviceStatus(String code, String description, boolean isActive) {
        this.code = code;
        this.description = description;
        this.isActive = isActive;
    }

    // 基础访问器
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    // 反向查找（根据code获取枚举实例）
    public static DeviceStatus fromCode(String code) {
        for (DeviceStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的设备状态码: " + code);
    }

    // 业务扩展方法
    public static DeviceStatus fromDescription(String description) {
        for (DeviceStatus status : values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的设备状态描述: " + description);
    }

    // 状态有效性校验
    public static void validateStatus(String code) {
        if (fromCode(code) == null) {
            throw new IllegalStateException("设备状态码校验失败: " + code);
        }
    }

    // 状态转换方法（示例）
    public DeviceStatus nextStatus() {
        switch (this) {
            case ONLINE: return MAINTENANCE;
            case MAINTENANCE: return OFFLINE;
            default: throw new UnsupportedOperationException("不支持的状态转换: " + this);
        }
    }
}
