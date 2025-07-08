package com.mirrormetech.corm.core.chat.infra.cache;

import com.mirrormetech.corm.common.redis.RedisService;
import com.mirrormetech.corm.core.chat.domain.repository.ChatSessionCacheRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 聊天会话 redis缓存接口实现类
 */
@Service
@RequiredArgsConstructor
public class RedisRepoImpl implements ChatSessionCacheRepo {

    private final RedisService redisService;


}
