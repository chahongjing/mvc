package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.LimitByCount;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.ErrorCodeEnum;
import com.zjy.service.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 重复请求校验
 */
@Slf4j
@Aspect
@Order(120)
@Component
public class LimitByCountAspect extends BaseAspect {
    @Resource
    private RedisUtils redisUtils;


    @Pointcut("@annotation(com.zjy.baseframework.annotations.LimitByCount)")
    public void limit() {
    }

    @Around(value = "limit() && @annotation(limitByCount)")
    public Object around(ProceedingJoinPoint pjp, LimitByCount limitByCount) throws Throwable {
        String key = getKey(RedisKeyUtils.LIMIT_OP, limitByCount.withUser(), limitByCount.withParam(), pjp);
        Long opNum = redisUtils.incrLimitExp(key, limitByCount.count(), limitByCount.timeout());
        if (opNum != null && opNum < 0) {
            throw new ServiceException(ErrorCodeEnum.SERVICE_BUSY);
        }
        try {
            return pjp.proceed();
        } finally {
            if (limitByCount.autoDecr() && redisUtils.hasKey(key)) {
                redisUtils.decr(key);
            }
        }
    }
}
