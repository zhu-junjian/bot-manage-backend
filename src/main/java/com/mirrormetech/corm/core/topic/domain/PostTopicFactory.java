package com.mirrormetech.corm.core.topic.domain;

import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;
import org.springframework.stereotype.Component;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Component
public class PostTopicFactory {

    public PostTopicDO create(PostTopic postTopic){
        PostTopicDO postTopicDO = new PostTopicDO();
        postTopicDO.setPostId(postTopic.getPostId());
        postTopicDO.setTopicId(postTopic.getTopicId());
        return postTopicDO;
    }
}
