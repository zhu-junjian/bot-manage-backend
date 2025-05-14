package com.mirrormetech.corm.core.topic.infra.persisitence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.topic.domain.Topic;
import com.mirrormetech.corm.core.topic.domain.repository.TopicRepository;
import com.mirrormetech.corm.core.topic.domain.vo.TopicVO;
import com.mirrormetech.corm.core.topic.infra.DO.TopicDO;
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
public class MyBatisTopicRepository implements TopicRepository {

    private final TopicMapper topicMapper;

    @Override
    public void insert(TopicDO topicDO) {
        topicMapper.insert(topicDO);
    }

    @Override
    public List<Topic> selectListById(Long topicId) {
        List<TopicDO> topicDOList = topicMapper.selectList(new QueryWrapper<TopicDO>().eq("id", topicId));
        List<Topic> topicList = new ArrayList<>();
        for (TopicDO topicDO : topicDOList) {
            Topic topic = new Topic();
            BeanUtils.copyProperties(topicDO, topic);
            topicList.add(topic);
        }
        return topicList;
    }

    @Override
    public List<TopicVO> selectList(QueryWrapper<TopicDO> queryWrapper) {
        List<TopicVO> topicVOList = new ArrayList<>();
        List<TopicDO> topicDOList = topicMapper.selectList(queryWrapper);
        for (TopicDO topicDO : topicDOList) {
            TopicVO topicVO = new TopicVO();
            BeanUtils.copyProperties(topicDO, topicVO);
            topicVOList.add(topicVO);
        }
        return topicVOList;
    }
}
