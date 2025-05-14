package com.mirrormetech.corm.core.category.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author spencer
 * @date 2025/04/27
 */
@Data
public class SecondLevelCategory {

    private Long id;

    private String name;

    private Long parentId;

}
