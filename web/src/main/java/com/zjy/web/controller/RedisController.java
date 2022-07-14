package com.zjy.web.controller;

import com.zjy.baseframework.common.Constants;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.common.utils.DateUtils;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.enums.RedisDataType;
import com.zjy.service.enums.RedisOpType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController {

    /**
     * 操作redis
     *
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
        user.setId(1L);
        if (opType != RedisOpType.GET && !Constants.SA_ADMIN.contains(user.getId())) {
            return BaseResult.error("没有操作权限");
        }
        if (dataType == null || opType == null || StringUtils.isBlank(key)) {
            return BaseResult.error("参数不能为空");
        }
        if (dataType == RedisDataType.HASH && StringUtils.isBlank(field) && opType != RedisOpType.DEL) {
            return BaseResult.error("参数不能为空");
        }
        if (dataType == RedisDataType.ZSET && score == null && (opType == RedisOpType.SET || opType == RedisOpType.ADD_ITEM)) {
            return BaseResult.error("参数不能为空");
        }
        log.info("{} optRedis.dataType:{},opType:{},key:{},field:{},value:{}", user.getId(), dataType, opType, key, field, value);
        if (opType == RedisOpType.DEL) {
            redisUtils.del(key);
            return BaseResult.ok();
        }
        if (opType == RedisOpType.TTL) {
            Long expire = redisUtils.getExpire(key);
            return BaseResult.ok(getTTL(expire));
        }
        Map<String, Object> result = null;
        switch (dataType) {
            // string
            case STRING:
                result = opString(opType, key, value);
                break;
            // list
            case LIST:
                result = opList(opType, key, value);
                break;
            // set
            case SET:
                result = opSet(opType, key, value);
                break;
            // zset
            case ZSET:
                result = opZset(opType, key, value, score);
                break;
            // hash
            case HASH:
                result = opHash(opType, key, field, value);
                break;
            default:
                break;
        }
        return BaseResult.ok(result);
    }

    private Map<String, Object> opString(RedisOpType opType, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        if (opType == RedisOpType.GET) {
            map.put("result", redisUtils.get(key));
            map.put("ttl", redisUtils.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            redisUtils.set(key, value);
        }
        return map;
    }

    private Map<String, Object> opList(RedisOpType opType, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        if (opType == RedisOpType.GET) {
            map.put("result", redisUtils.sMembers(key));
            map.put("ttl", redisUtils.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            redisUtils.del(key);
            if (StringUtils.isBlank(value)) return map;
            List<String> list = Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            redisUtils.sAdd(key, list.toArray(new String[0]));
        } else if (opType == RedisOpType.ADD_ITEM) {
            redisUtils.sAdd(key, value);
        } else if (opType == RedisOpType.DEL_ITEM) {
            redisUtils.sRemove(key, value);
        }
        return map;
    }

    private Map<String, Object> opSet(RedisOpType opType, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        if (opType == RedisOpType.GET) {
            map.put("result", redisUtils.range(key, 0, -1));
            map.put("ttl", redisUtils.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            redisUtils.del(key);
            if (StringUtils.isBlank(value)) return map;
            String[] list = (String[]) Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).toArray();
            redisUtils.lPushAll(key, list);
        } else if (opType == RedisOpType.ADD_ITEM) {
            redisUtils.lPushAll(key, value);
        } else if (opType == RedisOpType.DEL_ITEM) {
            redisUtils.lRemove(key, 0, value);
        }
        return map;
    }

    private Map<String, Object> opZset(RedisOpType opType, String key, String value, Double score) {
        Map<String, Object> map = new HashMap<>();
        if (opType == RedisOpType.GET) {
            map.put("result", redisUtils.range(key, 0, -1));
            map.put("ttl", redisUtils.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            redisUtils.del(key);
            if (StringUtils.isBlank(value)) return map;
            Map<String, Double> set = new HashMap<>();
            List<String> list = Arrays.stream(value.split("\\|")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            for (String item : list) {
                set.put(item, score);
            }
            redisUtils.zAddSet(key, set);
        } else if (opType == RedisOpType.ADD_ITEM) {
            redisUtils.zAdd(key, value, score);
        } else if (opType == RedisOpType.DEL_ITEM) {
            redisUtils.zRemove(key, value);
        }
        return map;
    }

    /**
     * @param opType
     * @param key
     * @param field
     * @param value
     * @return
     */
    private Map<String, Object> opHash(RedisOpType opType, String key, String field, String value) {
        Map<String, Object> map = new HashMap<>();
        if (opType == RedisOpType.GET) {
            map.put("result", redisUtils.hGet(key, field));
            map.put("ttl", redisUtils.getExpire(key));
        } else if (opType == RedisOpType.SET) {
            redisUtils.hSet(key, field, value);
        }
        return map;
    }

    private String getTTL(Long second) {
        if (second == null) {
            return "error";
        }
        if (second == -2) {
            return "key not exists";
        }
        if (second == -1) {
            return "forever";
        }
        return DateUtils.getTimeFromLong(second * 1000);
    }
}
