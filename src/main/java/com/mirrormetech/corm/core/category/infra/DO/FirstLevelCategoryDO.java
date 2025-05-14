package com.mirrormetech.corm.core.category.infra.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
@TableName("first_level_category")
public class FirstLevelCategoryDO {

    /**
     * 大类主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 大类名称
     */
    @TableField("name")
    private String name;
}
