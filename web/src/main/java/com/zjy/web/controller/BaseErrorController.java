package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//@RestController
public class BaseErrorController implements ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * 默认错误
     */
    private static final String path_default = "/error";

    /**
     * JSON格式错误信息
     */
    @RequestMapping(value = path_default)
    public BaseResult error(HttpServletRequest request) {
        ServletWebRequest requestAttributes = new ServletWebRequest(request);
        Map<String, Object> body = this.errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.defaults());
        String path = (String)body.get("path");
        return BaseResult.error("访问路径被外星人截走了！path：" + path);
    }
}
