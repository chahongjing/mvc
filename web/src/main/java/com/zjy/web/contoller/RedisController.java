package com.zjy.web.contoller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.enums.RedisDataType;
import com.zjy.service.enums.RedisOpType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 操作redis
     * @param dataType
     * @param opType
     * @param key
     * @param field
     * @param value
     * @return
     */
    @PostMapping("/optRedis")
    public BaseResult<Object> optRedis(RedisDataType dataType, RedisOpType opType, String key, String field, String value, Double score) {
        UserInfo user = new UserInfo();
        List<String> canOperaterList = Arrays.asList("3150270580", "1269590795");
        if(opType != RedisOpType.GET && !canOperaterList.contains(user.getId())) {
            return BaseResult.error("没有操作权限");
        }
        if(dataType == null || opType == null || StringUtils.isBlank(key)) {
            return BaseResult.error("参数不能为空");
        }
        if(dataType == RedisDataType.HASH && StringUtils.isBlank(field)) {
            return BaseResult.error("参数不能为空");
        }
        if(dataType == RedisDataType.ZSET && score == null) {
            return BaseResult.error("参数不能为空");
        }
        log.warn("{} optRedis.dataType:{},opType:{},key:{},field:{},value:{}", user.getId(), dataType, opType, key, field, value);
        if(opType == RedisOpType.DEL) {
            stringRedisTemplate.delete(key);
            return BaseResult.ok();
        }
        Map<String, Object> result = null;
        switch (dataType) {
            // string
            case STRING: result = opString(opType, key, value); break;
            // list
            case LIST: result = opList(opType, key, value); break;
                // set
            case SET: result = opSet(opType, key, value); break;
            // zset
            case ZSET: result = opZset(opType, key, value, score); break;
            // hash
            case HASH: result = opHash(opType, key, field, value); break;
            default: break;
        }
        return BaseResult.ok(result);
    }

    private Map<String, Object> opString(RedisOpType opType, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        if(opType == RedisOpType.GET) {
            map.put("result", stringRedisTemplate.opsForValue().get(key));
            map.put("ttl", stringRedisTemplate.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            stringRedisTemplate.opsForValue().set(key, value);
        }
        return map;
    }

    private Map<String, Object> opList(RedisOpType opType, String key, String value) {
        // todo: test
        Map<String, Object> map = new HashMap<>();
        if(opType == RedisOpType.GET) {
            map.put("result", stringRedisTemplate.opsForSet().members(key));
            map.put("ttl", stringRedisTemplate.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            stringRedisTemplate.delete(key);
            if(StringUtils.isBlank(value)) return map;
            List<String> list = Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            stringRedisTemplate.opsForSet().add(key, list.toArray(new String[0]));
        } else if(opType == RedisOpType.ADD_ITEM) {
            stringRedisTemplate.opsForSet().add(key, value);
        } else if(opType == RedisOpType.DEL_ITEM) {
            stringRedisTemplate.opsForSet().remove(key, value);
        }
        return map;
    }

    private Map<String, Object> opSet(RedisOpType opType, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        if(opType == RedisOpType.GET) {
            map.put("result", stringRedisTemplate.opsForList().range(key, 0, -1));
            map.put("ttl", stringRedisTemplate.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            stringRedisTemplate.delete(key);
            if(StringUtils.isBlank(value)) return map;
            List<String> list = Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            stringRedisTemplate.opsForList().leftPushAll(key, list);
        } else if(opType == RedisOpType.ADD_ITEM) {
            stringRedisTemplate.opsForList().leftPush(key, value);
        } else if(opType == RedisOpType.DEL_ITEM) {
            stringRedisTemplate.opsForList().remove(key, 0, value);
        }
        return map;
    }

    private Map<String, Object> opZset(RedisOpType opType, String key, String value, double score) {
        // todo: test
        Map<String, Object> map = new HashMap<>();
        if(opType == RedisOpType.GET) {
            map.put("result", stringRedisTemplate.opsForZSet().range(key, 0, -1));
            map.put("ttl", stringRedisTemplate.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            stringRedisTemplate.delete(key);
            if(StringUtils.isBlank(value)) return map;
            Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
            ZSetOperations.TypedTuple<String> zsetItem;
            List<String> list = Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            for (String item : list) {
                set.add(new DefaultTypedTuple<>(item, score));
            }
        stringRedisTemplate.opsForZSet().add(key, set);
        } else if(opType == RedisOpType.ADD_ITEM) {
            stringRedisTemplate.opsForZSet().add(key, value, score);
        } else if(opType == RedisOpType.DEL_ITEM) {
            stringRedisTemplate.opsForZSet().remove(key, value);
        }
        return map;
    }

    /**
     *
     * @param opType
     * @param key
     * @param field
     * @param value
     * @return
     */
    private Map<String, Object> opHash(RedisOpType opType, String key, String field, String value) {
        // todo: test
        Map<String, Object> map = new HashMap<>();
        if(opType == RedisOpType.GET) {
            map.put("result", stringRedisTemplate.opsForHash().get(key, field));
            map.put("ttl", stringRedisTemplate.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            stringRedisTemplate.opsForHash().put(key, field, value);
        }
        return map;
    }
}
