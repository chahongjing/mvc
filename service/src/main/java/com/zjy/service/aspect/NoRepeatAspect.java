package com.zjy.service.aspect;

import com.alibaba.fastjson.JSON;
import com.zjy.baseframework.annotations.NoRepeatOp;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.common.shiro.IUserInfo;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.service.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 重复请求校验
 */
@Slf4j
@Aspect
@Order(130)
@Component
public class NoRepeatAspect {
    @Resource
    private RedisUtils redisUtils;

    // 排除不能序列化的类
    Class[] list = new Class[]{HttpSession.class, BindingResult.class, HttpServletResponse.class,
            HttpServletRequest.class};

    @Pointcut("@annotation(com.zjy.baseframework.annotations.NoRepeatOp)")
    public void noRepeat() {
    }

    @Around(value = "noRepeat() && @annotation(noRepeatOp)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatOp noRepeatOp) throws Throwable {
//        Signature signature = pjp.getSignature();
//        MethodSignature methodSignature = (MethodSignature)signature;
//        Method targetMethod = methodSignature.getMethod();
//        NoRepeatOp noRepeatOp = targetMethod.getAnnotation(NoRepeatOp.class);
        String key = getKey(noRepeatOp, pjp);
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

    private String getKey(NoRepeatOp noRepeatOp, ProceedingJoinPoint pjp) {
        String argsJson = null;
        Long userId = null;
        if(noRepeatOp.withUser()) {
            IUserInfo shiroUser = ShiroRealmUtils.getCurrentUser();
            userId = shiroUser == null ? null : shiroUser.getId();
        }
        if(noRepeatOp.withParam()) {
            Object[] argArr = Arrays.stream(pjp.getArgs()).filter(this::includeArg).toArray();
            argsJson = JSON.toJSONString(argArr);
        }
        String hash = DigestUtils.md5Hex(pjp.toShortString() + userId + argsJson);
        return RedisKeyUtils.REPEAT_OP + ":" + hash;
    }

    private boolean includeArg(Object arg) {
        if (arg == null) return true;
        for (Class clazz : list) {
            if (clazz.isAssignableFrom(arg.getClass())) return false;
        }
        return true;
    }
}
