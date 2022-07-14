package com.zjy.service.aspect;

import com.zjy.common.shiro.IUserInfo;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.common.utils.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class BaseAspect {
    @Autowired
    private JsonUtils jsonUtils;

    // 排除不能序列化的类
    Class[] list = new Class[]{HttpSession.class, BindingResult.class, HttpServletResponse.class,
            HttpServletRequest.class};

    protected String getKey(String keyPrefix, boolean withUser, boolean withParam, ProceedingJoinPoint pjp) {
        String argsJson = null;
        Long userId = null;
        if(withUser) {
            IUserInfo shiroUser = ShiroRealmUtils.getCurrentUser();
            userId = shiroUser == null ? null : shiroUser.getId();
        }
        if(withParam) {
            argsJson = jsonUtils.toJSON(Arrays.stream(pjp.getArgs()).filter(this::includeArg).toArray());
        }
        String hash = DigestUtils.md5Hex(pjp.toShortString() + argsJson);
        return String.format("%s:%d:%s", keyPrefix, userId, hash);
    }

    private boolean includeArg(Object arg) {
        if (arg == null) return true;
        for (Class clazz : list) {
            if (clazz.isAssignableFrom(arg.getClass())) return false;
        }
        return true;
    }
}
