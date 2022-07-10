package com.zjy.web.controller;

import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.common.EnumEditor;
import com.zjy.common.MyCustomDateEditor;
import com.zjy.common.MyCustomZonedDateEditor;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.entity.model.UserInfo;
import com.zjy.common.utils.JsonUtils;
import com.zjy.service.common.RedisUtils;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.common.utils.EnumUtils;
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
        model.addAttribute("user", getCurrentUser());
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

    public static UserInfo getCurrentUser() {
        return BaseServiceImpl.getCurrentUser();
    }

    public static boolean isPermitted(String permission) {
        return ShiroRealmUtils.isPermitted(permission);
    }
}
