package com.zjy.common.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager<K, V> implements CacheManager {
    private RedisTemplate<K, V> redisTemplate;

    public RedisCacheManager(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<>(name, redisTemplate);
    }
}
