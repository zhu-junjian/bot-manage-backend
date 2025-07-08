package com.mirrormetech.corm.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 存储字符串
    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 获取字符串
    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 存储Hash
    public void setHash(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    // 获取Hash
    public String getHash(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }
}
