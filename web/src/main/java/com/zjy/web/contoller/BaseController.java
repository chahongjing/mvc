package com.zjy.web.contoller;

import com.zjy.common.MyCustomDateEditor;
import com.zjy.common.MyCustomZonedDateEditor;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.entity.model.UserInfo;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/2.
 */
public class BaseController {

    @ModelAttribute
    public void init(Model model) {
        model.addAttribute("user", ShiroRealmUtils.getCurrentUser());
    }

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
        binder.registerCustomEditor(ZonedDateTime.class, new MyCustomZonedDateEditor());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    public static UserInfo getCurrentUser() {
        return ShiroRealmUtils.getCurrentUser();
    }

    public static boolean isPermitted(String permission) {
        return ShiroRealmUtils.isPermitted(permission);
    }
}
