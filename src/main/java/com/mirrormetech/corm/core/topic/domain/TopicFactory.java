package com.mirrormetech.corm.core.topic.domain;

import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.topic.domain.dto.TopicDTO;
import com.mirrormetech.corm.core.topic.infra.DO.TopicDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Component
public class TopicFactory {

    public TopicDO create(TopicDTO topicDTO) {
        TopicDO topicDO = new TopicDO();
        topicDO.setName(topicDTO.getTopicName());
        topicDO.setCreate_time(ZonedDateTimeUtils.getCurrentTimeInCST());
        topicDO.setStatus(0);
        return topicDO;
    }
}
