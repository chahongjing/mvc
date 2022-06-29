package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Order(110)
@Component
public class RedisCacheAspect {
    @Lazy
    @Resource
    private RedisTemplate<String, Object> objRedisTemplate;
    Pattern pattern = Pattern.compile("\\#\\{([^}]+)\\}");

    @Pointcut("@annotation(com.zjy.baseframework.annotations.RedisCache)")
    public void cache() {
    }

    @Around(value = "cache() && @annotation(redisCache)")
    public Object around(ProceedingJoinPoint pjp, RedisCache redisCache) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
//        RedisCache redisCache = targetMethod.getAnnotation(RedisCache.class);
        if(redisCache == null) {
            return pjp.proceed();
        }
        Parameter[] parameters = targetMethod.getParameters();
        String key = getKey(redisCache, parameters, pjp.getArgs());
        Object result = objRedisTemplate.opsForValue().get(key);
        // 没有值
        if(result == null) {
            Boolean exists = objRedisTemplate.hasKey(key);
            // 缓存的null，直接返回
            if (exists != null && exists) {
                return result;
            } else {
                // 没有key
                result = pjp.proceed();
                objRedisTemplate.opsForValue().set(key, result, redisCache.expire(), redisCache.timeUnit());
            }
        }
        return result;
    }

    private String getKey(RedisCache redisCache, Parameter[] parameters, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        String key = redisCache.key();
        if(parameters != null && parameters.length > 0 && key.contains("#{")) {
            for (int i = 0; i < parameters.length; i++) {
                map.put(parameters[i].getName(), args[i]);
            }
        }
        Matcher matcher = pattern.matcher(key);
        String newKey = key;
        while (matcher.find()) {
            if(map.containsKey(matcher.group(1))) {
                newKey = newKey.replace(matcher.group(0), String.valueOf(map.get(matcher.group(1))));
            }
        }
        return newKey;
    }
}


