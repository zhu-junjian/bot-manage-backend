package com.mirrormetech.corm.core.topic.service;

import com.mirrormetech.corm.core.topic.domain.dto.TopicDTO;
import com.mirrormetech.corm.core.topic.domain.vo.TopicVO;

import java.util.List;

public interface TopicService {

    void create(TopicDTO topicDTO);

    /**
     * 获取所有状态为正常的话题
     * @return topics
     */
    List<TopicVO> getAllNormalTopics();
}
