package com.zjy.web.controller;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.ErrorCodeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.common.common.EnumEditor;
import com.zjy.common.common.MyCustomDateEditor;
import com.zjy.common.common.MyCustomZonedDateEditor;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.common.utils.EnumUtils;
import com.zjy.common.utils.JsonUtils;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.RedisUtils;
import com.zjy.service.component.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/2.
 */
public class BaseController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected RedisUtils redisUtils;
    @Autowired
    protected JsonUtils jsonUtils;

    @ModelAttribute
    public void init(Model model) {
        model.addAttribute("user", getCurrentUserWithNoEx());
    }

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
        binder.registerCustomEditor(ZonedDateTime.class, new MyCustomZonedDateEditor());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
        // 添加枚举字段解析
        for (Class<IBaseEnum> enumClass : EnumUtils.getEnumList()) {
            binder.registerCustomEditor(enumClass, new EnumEditor(enumClass));
        }
    }

    public static UserInfo getCurrentUserWithNoEx() {
        return BaseServiceImpl.getCurrentUser();
    }

    public static UserInfo getCurrentUser() {
        UserInfo currentUser = getCurrentUserWithNoEx();
        if(currentUser == null) {
            throw new ServiceException(ErrorCodeEnum.UNAUTHENTICATION);
        }
        return currentUser;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isPermitted(String permission) {
        return ShiroRealmUtils.isPermitted(permission);
    }
}
