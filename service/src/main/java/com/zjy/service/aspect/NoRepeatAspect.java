package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.NoRepeatOp;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.service.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 重复请求校验
 */
@Slf4j
@Aspect
@Order(130)
@Component
public class NoRepeatAspect extends BaseAspect{
    @Resource
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.zjy.baseframework.annotations.NoRepeatOp)")
    public void noRepeat() {
    }

    @Around(value = "noRepeat() && @annotation(noRepeatOp)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatOp noRepeatOp) throws Throwable {
        String key = getKey(RedisKeyUtils.REPEAT_OP, noRepeatOp.withUser(), noRepeatOp.withParam(), pjp);
        boolean r = redisUtils.lock(key, "1", noRepeatOp.timeout(), TimeUnit.SECONDS);
        if (r) {
            try {
                return pjp.proceed();
            } finally {
                redisUtils.del(key);
            }
        }
        throw new ServiceException("重复请求");
    }
}
