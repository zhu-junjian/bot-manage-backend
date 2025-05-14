package com.mirrormetech.corm.core.topic;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.topic.domain.dto.TopicDTO;
import com.mirrormetech.corm.core.topic.domain.vo.TopicVO;
import com.mirrormetech.corm.core.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@RestController
@RequestMapping("/api/posts/topic")
@RequiredArgsConstructor
public class TopicController {

    @Qualifier("topicServiceImpl")
    private final TopicService topicService;

    @PostMapping()
    public ApiResult createTopic(@RequestBody TopicDTO topicDTO) {
        topicService.create(topicDTO);
        return ApiResult.success();
    }

    @GetMapping()
    public ApiResult<List<TopicVO>> updateTopic() {
        List<TopicVO> topics = topicService.getAllNormalTopics();
        return ApiResult.success(topics);
    }
}
