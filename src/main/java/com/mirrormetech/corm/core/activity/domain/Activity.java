package com.mirrormetech.corm.core.activity.domain;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

/**
 * 手信活动 领域模型
 * 将引起领域模型状态发生变更的行为封装在领域模型内部
 * 行为包含：
 * 1.活动创建
 * 2.活动是否过期
 * 3.活动延期
 * 4.关闭活动
 */
public class Activity {

    //活动背景图\视频 活动展示内容
    private String backgroundUrl;

    //活动名称
    private String name;

    //活动标题
    private String heading;

    //活动副标题
    private String subheading;

    // 活动开始时间
    private LocalDateTime startTime;

    // 活动结束时间
    private LocalDateTime endTime;

    //状态 0-正常开启 1-关闭
    private Integer status;

    //活动状态 true-开启 false-关闭
    private Boolean enable;

    //私有的无参构造器
    public Activity(){}

    /**
     * if the activity is enabled
     * compare now to endTime
     * @return #boolean if the activity is enabled
     * @see LocalDateTime#isAfter(ChronoLocalDateTime)
     */
    public boolean isEnable(){
        return this.endTime.isAfter(LocalDateTime.now());
    }

    /**
     * commence this activity,set current time to startTime
     */
    public void commence(){
        this.startTime = LocalDateTime.now();
        this.enable = true;
        this.status = 0;
    }

    public void unCommence(){
        this.endTime = LocalDateTime.now();
        this.enable = false;
        this.status = 1;
    }

    public void createActivity(){}
}
