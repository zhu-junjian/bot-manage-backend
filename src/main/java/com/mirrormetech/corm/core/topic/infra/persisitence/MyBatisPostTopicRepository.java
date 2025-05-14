package com.mirrormetech.corm.core.topic.infra.persisitence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.topic.domain.PostTopic;
import com.mirrormetech.corm.core.topic.domain.repository.PostTopicRepository;
import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Repository
@RequiredArgsConstructor
public class MyBatisPostTopicRepository implements PostTopicRepository {

    private final PostTopicMapper postTopicMapper;

    @Override
    public void save(PostTopicDO postTopicDO) {
        postTopicMapper.insert(postTopicDO);
    }

    @Override
    public List<PostTopic> selectList(Long postId) {
        List<PostTopic> postTopicList = new ArrayList<>();
        List<PostTopicDO> postTopicDOList = postTopicMapper.selectList(new QueryWrapper<PostTopicDO>().eq("post_id", postId));
        postTopicDOList.forEach(postTopicDO -> {
            PostTopic postTopic = new PostTopic();
            BeanUtils.copyProperties(postTopicDO, postTopic);
            postTopicList.add(postTopic);
        });
        return postTopicList;
    }
}
