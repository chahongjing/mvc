package com.zjy.service.aspect;

import com.alibaba.fastjson.JSON;
import com.zjy.baseframework.annotations.NoRepeatOp;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.service.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 重复请求校验
 */
@Slf4j
@Aspect
@Order(120)
@Component
public class NoRepeatAspect {
    @Resource
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.zjy.baseframework.annotations.NoRepeatOp)")
    public void noRepeat() {
    }

    @Around(value = "noRepeat()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        NoRepeatOp noRetryClass = targetMethod.getAnnotation(NoRepeatOp.class);
        int timeout = noRetryClass.timeout();

        Object[] args = pjp.getArgs();
        String argsJson = JSON.toJSONString(args);
        String hash = DigestUtils.md5Hex(pjp.toShortString() + argsJson);
        String key = RedisKeyUtils.REPEAT_OP + ":" + hash;
        boolean r = redisUtils.lock(key, "1", timeout, TimeUnit.SECONDS);
        if (r) {
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                throw t;
            } finally {
                redisUtils.del(key);
            }
        }
        throw new ServiceException("重复请求");
    }
}
