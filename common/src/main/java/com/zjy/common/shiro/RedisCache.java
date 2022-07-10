package com.zjy.common.shiro;

import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.common.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {
    private RedisTemplate<K, V> redisTemplate;
    private final static String PREFIX = RedisKeyUtils.KEY_PREFIX + "shiro-cache:";
    private String cacheKey;
    private long globExpire = 30;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisCache(final String name) {
        this.cacheKey = PREFIX + name + ":";
    }

    @Override
    public V get(K key) throws CacheException {
        log.debug("Shiro从缓存中获取数据KEY值["+key+"]");
        getRedisTemplate().boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MINUTES);
        return getRedisTemplate().boundValueOps(getCacheKey(key)).get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V old = get(key);
        getRedisTemplate().boundValueOps(getCacheKey(key)).set(value);
        return old;
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = get(key);
        getRedisTemplate().delete(getCacheKey(key));
        return old;
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(keys());

    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return getRedisTemplate().keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }

    private RedisTemplate<K, V> getRedisTemplate() {
        if(redisTemplate != null) return redisTemplate;
        this.redisTemplate = (RedisTemplate<K, V>)SpringContextHolder.getBean("shiroObjRedisTemplate");
        return redisTemplate;
    }
}