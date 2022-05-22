package com.zjy.service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author chahongjing
 * @create 2016-12-10 17:45
 */
//@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class MyControllerAdvice {//implements ResponseBodyAdvice<String>
//     private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseResultBody.class;
//     MethodParameter returnType;
// return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    /**
     * 全局异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler
//    @ResponseBody
    public void processException(HttpServletRequest request, HttpServletResponse response, Exception ex, HandlerMethod handler) {
        //20191217
        ControllerAspect.logException(request, response, handler.getMethod(), ex);
    }

    /**
     * 没有登录时处理，shiro框架已处理，在自定义授权中会自动返回未登录状态码，不会走这里
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
//    @ExceptionHandler(UnauthenticatedException.class)
//    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
//    public ModelAndView processUnauthenticatedException(HttpServletRequest request, HttpServletResponse response, UnauthenticatedException ex) {
//        return null;
//    }

    /**
     * 没有权限时处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ModelAndView processUnauthorizedException(HttpServletRequest request, HttpServletResponse response, UnauthorizedException ex) {
//        return null;
//    }
}
