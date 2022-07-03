package com.zjy.service.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zjy.baseframework.annotations.RedisCache;
import com.zjy.baseframework.common.RedisKeyUtils;
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
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
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
        if(redisCache == null) {
            return pjp.proceed();
        }
        Parameter[] parameters = targetMethod.getParameters();
        String key = getKey(redisCache, parameters, pjp);
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
        } else {
            // 如果是fastjson需要将jsonobject转为对应的类型，如是是jackson，则不需要转换
//            Class<?> returnType = targetMethod.getReturnType();
//            Class<?> trueType = getTrueType(targetMethod);
//            if(Collection.class.isAssignableFrom(returnType)) {
//                result = JSON.parseArray(JSON.toJSONString(result), trueType);
//            } else if(Collection.class.isAssignableFrom(returnType)) {
//                //result = JSON.parseArray(JSON.toJSONString(result), trueType);
//            } else if(result instanceof JSONObject) {
//                result = JSON.toJavaObject((JSONObject)result, trueType);
//            }
        }
        return result;
    }

    public Class getTrueType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class realType;
        if(Collection.class.isAssignableFrom(method.getReturnType())) {
            realType = (Class)((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else {
            realType = method.getReturnType();
        }
        return realType;
    }

    private String getKey(RedisCache redisCache, Parameter[] parameters, ProceedingJoinPoint pjp) {
        Map<String, Object> map = new HashMap<>();
        String key = RedisKeyUtils.KEY_PREFIX + pjp.toShortString() + ":" + redisCache.key();
        if(parameters != null && parameters.length > 0 && key.contains("#{")) {
            for (int i = 0; i < parameters.length; i++) {
                map.put(parameters[i].getName(), pjp.getArgs()[i]);
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


