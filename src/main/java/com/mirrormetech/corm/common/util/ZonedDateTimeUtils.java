package com.mirrormetech.corm.common.util;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author spencer
 * @date 2025/04/28
 */
public final class ZonedDateTimeUtils {

    // 私有构造方法，防止实例化
    private ZonedDateTimeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // region 东八区时间操作
    /**
     * 获取当前东八区时间（精确到秒），返回 SQL Timestamp 类型
     *
     * @return 当前东八区时间的 Timestamp 对象（UTC 时间戳）
     */
    public static Timestamp getCurrentTimeInCST() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
                .withNano(0); // 清除纳秒，保留到秒
        return Timestamp.from(zdt.toInstant());      // 转换为 UTC 时间戳
    }

    /**
     * 将 Timestamp 转换为东八区 ZonedDateTime
     */
    public static ZonedDateTime convertTimestampToCST(Timestamp timestamp) {
        return timestamp.toInstant()
                .atZone(ZoneId.of("Asia/Shanghai"));
    }

    /**
     * 格式化时间为字符串（东八区时间）
     */
    public static String formatDateTime(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    // endregion

    // region 时间比对操作（支持 ZonedDateTime 和 Timestamp）
    /**
     * 判断 time1 是否早于 time2（自动适配 ZonedDateTime 和 Timestamp）
     */
    public static boolean isBefore(Object time1, Object time2) {
        ZonedDateTime zdt1 = toZonedDateTime(time1);
        ZonedDateTime zdt2 = toZonedDateTime(time2);
        return zdt1.isBefore(zdt2);
    }

    /**
     * 判断 time1 是否晚于 time2（自动适配 ZonedDateTime 和 Timestamp）
     */
    public static boolean isAfter(Object time1, Object time2) {
        ZonedDateTime zdt1 = toZonedDateTime(time1);
        ZonedDateTime zdt2 = toZonedDateTime(time2);
        return zdt1.isAfter(zdt2);
    }

    /**
     * 比较两个时间，返回结果：
     * -1 : time1 在 time2 之前
     *  0 : 时间相等
     *  1 : time1 在 time2 之后
     */
    public static int compare(Object time1, Object time2) {
        ZonedDateTime zdt1 = toZonedDateTime(time1);
        ZonedDateTime zdt2 = toZonedDateTime(time2);
        return zdt1.compareTo(zdt2);
    }

    // 辅助方法：将不同类型时间统一转为 ZonedDateTime（东八区）
    private static ZonedDateTime toZonedDateTime(Object time) {
        if (time instanceof ZonedDateTime) {
            return (ZonedDateTime) time;
        } else if (time instanceof Timestamp) {
            return ((Timestamp) time).toInstant()
                    .atZone(ZoneId.of("Asia/Shanghai"));
        } else {
            throw new IllegalArgumentException("Unsupported time type: " + time.getClass());
        }
    }
    // endregion
}
