package com.mirrormetech.corm.core.banner.infra.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
@TableName("banner")
public class BannerDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("heading")
    private String heading;

    @TableField("subheading")
    private String subheading;

    @TableField("background_url")
    private String backgroundUrl;

    @TableField("target_url")
    private String targetUrl;

    @TableField("weight")
    private Integer weight;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("create_time")
    private Timestamp createTime;

    @TableField("banner_type")
    private Integer bannerType;

    @TableField("status")
    private Integer status;
}
