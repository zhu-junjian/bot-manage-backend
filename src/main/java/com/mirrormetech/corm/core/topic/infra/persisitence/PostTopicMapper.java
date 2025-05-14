package com.mirrormetech.corm.core.topic.infra.persisitence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.topic.infra.DO.PostTopicDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PostTopicMapper extends BaseMapper<PostTopicDO> {
}
