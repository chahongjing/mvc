package com.zjy.baseframework.common;

public class RedisKeyUtils {
    public static final String KEY_PREFIX = "mvc:";
    public static final String SWITCH_KEY = KEY_PREFIX + "SWITCH_KEY";
    public static final String ZSET_CACHE_KEY = KEY_PREFIX + "cache:zset";
    public static final String SET_CACHE_KEY = KEY_PREFIX + "cache:set";
    public static final String KV_CONFIG = KEY_PREFIX + "kvconfig";
    public static final String REPEAT_OP = KEY_PREFIX + "repeat_op";
}
