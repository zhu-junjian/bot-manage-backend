package com.mirrormetech.corm.core.topic.domain.repository;

import com.mirrormetech.corm.core.topic.domain.PostTopic;
import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;

import java.util.List;

public interface PostTopicRepository {

    void save(PostTopicDO postTopicDO);

    List<PostTopic> selectList(Long postId);
}
