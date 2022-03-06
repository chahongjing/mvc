package com.zjy.service.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisUtils {

    private static final String SET_KEY = "mvc:cache:set";
    private static final String ZSET_KEY = "mvc:cache:zset";
    private static final int timeout_second = 3600;

    @Resource
    @Lazy
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean lock(String key, String value, int timeout, TimeUnit timeUnit) {
        Boolean r = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        return r != null && r;
    }

    // region string
    public boolean del(String key) {
        Boolean r = stringRedisTemplate.delete(key);
        return r == null || r;
    }

    public void set(String key, String value, long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Long del(List<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    public Boolean expire(String key, long time) {
        return stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public Long incr(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    public Long decr(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }
    // endregion

    // region hash
    public Object hGet(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    public Boolean hSet(String key, String hashKey, Object value, long time) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    public void hSet(String key, String hashKey, Object value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Map<Object, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        stringRedisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    public void hSetAll(String key, Map<String, ?> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    public void hDel(String key, Object... hashKey) {
        stringRedisTemplate.opsForHash().delete(key, hashKey);
    }

    public Boolean hHasKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Long hIncr(String key, String hashKey, Long delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Long hDecr(String key, String hashKey, Long delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, -delta);
    }
    // endregion

    // region set
    public Set<String> sMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public Long sAdd(String key, String... values) {
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    public Long sAdd(String key, long time, String... values) {
        Long count = stringRedisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    public Boolean sIsMember(String key, Object value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public Long sSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public Long sRemove(String key, Object... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }
    // endregion

    // region list
    public List<String> lRange(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    public Long lSize(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    public String lIndex(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    public Long lPush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    public Long lPush(String key, String value, long time) {
        Long index = stringRedisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    public Long lPushAll(String key, String... values) {
        return stringRedisTemplate.opsForList().rightPushAll(key, values);
    }

    public Long lPushAll(String key, Long time, String... values) {
        Long count = stringRedisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    public Long lRemove(String key, long count, String value) {
        return stringRedisTemplate.opsForList().remove(key, count, value);
    }
    // endregion

    // region zet
    public void zAdd(String key, String value, double score) {
        stringRedisTemplate.opsForZSet().add(key, value, score);
    }
    public void zAddSet(String key, Map<String, Double> map) {
        Set<ZSetOperations.TypedTuple<String>> set = map.entrySet().stream().map(entry -> new DefaultTypedTuple<>(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
        stringRedisTemplate.opsForZSet().add(key, set);
    }
    public void zRemove(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
        stringRedisTemplate.opsForZSet().range(key, 0, -1); // -1表示所有元素
        // zCard，reverseRank，count，reverseRange
        stringRedisTemplate.opsForZSet().removeRange(key, 0, 20);
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, 100);
    }
    public Set<String> range(String key, double minScore, double maxScore) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }
    // endregion

    // region zset opt
    public void addItem(String instanceId) {
        log.info("添加超时任务：{}", instanceId);
        String key = getZsetCacheKey();
        long deadline = System.currentTimeMillis() + timeout_second * 1000L;
        stringRedisTemplate.opsForZSet().remove(key, instanceId);
        stringRedisTemplate.opsForZSet().add(key, instanceId, deadline);
    }

    public void removeItem(String instanceId) {
        log.info("删除超时任务：{}", instanceId);
        String key = getZsetCacheKey();
        stringRedisTemplate.opsForZSet().remove(key, instanceId);
    }

    public int handleTimeoutGov(int limit) {
        log.info("开始处理超时工单");
        String key = getZsetCacheKey();
        Set<String> instanceIds = stringRedisTemplate.opsForZSet().rangeByScore(key, 0, System.currentTimeMillis(), 0, limit);
        if (CollectionUtils.isEmpty(instanceIds)) {
            return 0;
        }
        log.info("开始处理超时工单：{}", JSON.toJSONString(instanceIds));
        for (String instanceId : instanceIds) {
            log.info("handleTimeoutGov. id: {}", instanceId);
            // todo: handle something
//            workOrderRemindService.remindAfterOperation(tenantId, userTenantId, wo, WorkOrderOperationEnum.TIMEOUT, null, 0L);
            removeItem(instanceId);
        }
        return instanceIds.size();
    }

    private String getZsetCacheKey() {
        return ZSET_KEY;
    }
    // endregion

    // region set opt
    public void test(String id) {
        String key = getSetCacheKey();
        stringRedisTemplate.opsForSet().add(key, id);
//        String[] idList = entry.getValue().stream().map(String::valueOf).toArray(String[]::new);
//        stringRedisTemplate.opsForSet().add(key, idList);
        Set<String> strings = stringRedisTemplate.opsForSet().distinctRandomMembers(key, 200L);
//        Set<String> codeList = stringRedisTemplate.opsForSet().members(key);
        if(strings != null) {
            for (String item : strings) {
                stringRedisTemplate.opsForSet().remove(key, item);
            }
        }
    }

    private String getSetCacheKey() {
        return SET_KEY;
    }
    // endregion
}
