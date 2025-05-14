package com.mirrormetech.corm.core.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.core.topic.domain.TopicFactory;
import com.mirrormetech.corm.core.topic.domain.dto.TopicDTO;
import com.mirrormetech.corm.core.topic.domain.repository.TopicRepository;
import com.mirrormetech.corm.core.topic.domain.vo.TopicVO;
import com.mirrormetech.corm.core.topic.infra.DO.TopicDO;
import com.mirrormetech.corm.core.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    @Qualifier("myBatisTopicRepository")
    private final TopicRepository MyBatisTopicRepository;

    private final TopicFactory topicFactory;

    @Override
    public void create(TopicDTO topicDTO) {
        //1 校验参数
        String newTopicName = topicDTO.getTopicName();
        if (newTopicName == null) {
            throw new ServiceException(ExceptionCode.MISSING_TOPIC_NAME);
        }
        List<TopicVO> allNormalTopics = getAllNormalTopics();
        if (allNormalTopics != null) {
            boolean nameExists = allNormalTopics.stream().anyMatch(topic -> topic.getName().equals(newTopicName));
            if (nameExists) {
                throw new ServiceException(ExceptionCode.DUPLICATED_TOPIC_NAME);
            }
        }
        //2 封装实体
        TopicDO topicDO = topicFactory.create(topicDTO);
        // 保存数据库
        MyBatisTopicRepository.insert(topicDO);
    }

    @Override
    public List<TopicVO> getAllNormalTopics() {
        List<TopicVO> topics = new ArrayList<TopicVO>();
        QueryWrapper<TopicDO> queryWrapper = new QueryWrapper<TopicDO>();
        queryWrapper.eq("status", 0);
        topics = MyBatisTopicRepository.selectList(queryWrapper);
        return topics;
    }
}
