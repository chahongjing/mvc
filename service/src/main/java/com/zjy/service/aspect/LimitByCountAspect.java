package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.LimitByCount;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.common.shiro.IUserInfo;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.common.utils.JsonUtils;
import com.zjy.service.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * 重复请求校验
 */
@Slf4j
@Aspect
@Order(120)
@Component
public class LimitByCountAspect {
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private JsonUtils jsonUtils;

    // 排除不能序列化的类
    Class[] list = new Class[]{HttpSession.class, BindingResult.class, HttpServletResponse.class,
            HttpServletRequest.class};

    @Pointcut("@annotation(com.zjy.baseframework.annotations.LimitByCount)")
    public void limit() {
    }

    @Around(value = "limit() && @annotation(limitByCount)")
    public Object around(ProceedingJoinPoint pjp, LimitByCount limitByCount) throws Throwable {
        String key = getKey(limitByCount, pjp);
        Long opNum = redisUtils.incrLimitExp(key, limitByCount.count(), limitByCount.expire());
        if(opNum != null && (Integer.parseInt(opNum.toString())) < 0) {
            throw new ServiceException("服务繁忙");
        }
        try {
            return pjp.proceed();
        } finally {
            if(redisUtils.hasKey(key)) {
                redisUtils.decr(key);
            }
        }
    }

//    @Around(value = "limit() && @annotation(limitByCount)")
//    public Object around(ProceedingJoinPoint pjp, LimitByCount limitByCount) throws Throwable {
//        String key = getKey(limitByCount, pjp);
//        Long opNum = redisUtils.incrEx(key, 3600);
//        if(opNum != null && (Integer.parseInt(opNum.toString())) > limitByCount.count()) {
//            redisUtils.decr(key);
//            throw new ServiceException("服务繁忙");
//        }
//        // 如果有人处理，则key续约，如果没有人下载，1个小时后自动删除，防止线上服务崩溃没有decrement导致服务不可用
////        redisUtils.expire(key, 1, TimeUnit.HOURS);
//        try {
//            return pjp.proceed();
//        } finally {
//            if(redisUtils.hasKey(key)) {
//                redisUtils.decr(key);
//            }
//        }
//    }

    private String getKey(LimitByCount limitByCount, ProceedingJoinPoint pjp) {
        String argsJson = null;
        Long userId = null;
        if(limitByCount.withUser()) {
            IUserInfo shiroUser = ShiroRealmUtils.getCurrentUser();
            userId = shiroUser == null ? null : shiroUser.getId();
        }
        if(limitByCount.withParam()) {
            Object[] argArr = Arrays.stream(pjp.getArgs()).filter(this::includeArg).toArray();
            argsJson = jsonUtils.toJSON(argArr);
        }
        String hash = DigestUtils.md5Hex(pjp.toShortString() + userId + argsJson);
        return RedisKeyUtils.LIMIT_OP + ":" + hash;
    }

    private boolean includeArg(Object arg) {
        if (arg == null) return true;
        for (Class clazz : list) {
            if (clazz.isAssignableFrom(arg.getClass())) return false;
        }
        return true;
    }
}
