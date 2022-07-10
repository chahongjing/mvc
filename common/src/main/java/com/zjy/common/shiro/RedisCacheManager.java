package com.zjy.common.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager<K, V> implements CacheManager {
    @Override
    public Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<>(name);
    }
}
