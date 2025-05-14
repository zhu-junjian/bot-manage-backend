package com.mirrormetech.corm.core.topic.infra.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author spencer
 * @date 2025/04/29
 */
@Data
@TableName("post_topic")
@AllArgsConstructor
@NoArgsConstructor
public class PostTopicDO {

    @TableField("post_id")
    private Long postId;

    @TableField("topic_id")
    private Long topicId;
}
