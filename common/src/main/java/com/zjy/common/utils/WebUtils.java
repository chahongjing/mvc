package com.zjy.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @date 2019-09-04 16:39:12
 */
public class WebUtils {
    public WebApplicationContext getWebApplicationContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }

    public static String getWebApplicationUrlPath() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (request.getRequestURL().toString().replace(request.getRequestURI(), StringUtils.EMPTY) + request.getContextPath());
    }

    public static boolean isAjax(WebRequest request) {
        return request != null && isAjax(request.getHeader("X-Requested-With"));
    }

    public static boolean isAjax(HttpServletRequest request) {
        return request != null && isAjax(request.getHeader("X-Requested-With"));
    }

    public static boolean isAjax(String ajaxHeader) {
        return "XMLHttpRequest".equalsIgnoreCase(ajaxHeader);
    }
}
