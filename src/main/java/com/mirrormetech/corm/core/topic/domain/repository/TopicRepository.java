package com.mirrormetech.corm.core.topic.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.topic.domain.Topic;
import com.mirrormetech.corm.core.topic.domain.vo.TopicVO;
import com.mirrormetech.corm.core.topic.infra.DO.TopicDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author spencer
 * @date 2025/04/28
 */
@Repository
@Mapper
public interface TopicRepository {

    void insert(TopicDO topicDO);

    /**
     * 按topicId查询话题详情
     * @param topicId
     * @return
     */
    List<Topic> selectListById(Long topicId);

    /**
     * 查询所有正常状态下的话题
     * @param queryWrapper
     * @return
     */
    List<TopicVO> selectList(QueryWrapper<TopicDO> queryWrapper);
}
