package com.zjy.service.aspect;

import com.alibaba.fastjson.JSON;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 日志拦截，要在spring-mvc.xml中添加<aop:aspectj-autoproxy proxy-target-class="true"/>
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class ControllerAspect {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    private static final String USER_EXP = "\\{user\\}";
    private static final String METHOD_EXP = "\\{method\\}";
    private static final String NEW_LINE = System.getProperty("line.separator", "\r\n");
    private static final String LOG_PARAMETER = "log_parameter";
    private static final String START_TIME = "__startTime";
    private static final String KEY_PARTTERN = "{%s}";

    /**
     * Controller层切点
     */
//    @Pointcut("@annotations(com.dmall.bll.annotations.LogMessage)")
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerAspect() {
        // 
    }

    /**
     * handle执行前置事件
     *
     * @param joinPoint
     */
    @Before("controllerAspect()")
    public void before(JoinPoint joinPoint) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
    }

    /**
     * handle执行未抛异常后置事件
     *
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(pointcut = "controllerAspect()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        Method method = getMethod(joinPoint);
        if (method == null) return;
//        LogMessage annotations = method.getAnnotation(LogMessage.class);
//        if (annotations == null || annotations.doLog()) {
        logRequest(request, response, method, ret);
//        }
    }

    /**
     * 抛出异常后处理事件
     *
     * @param joinPoint
     * @return
     */
//    @AfterThrowing(pointcut = "controllerAspect()", throwing = "ex")
//    public void AfterThrowing(JoinPoint joinPoint, Exception ex) {
//        logException(request, response, getMethod(joinPoint), ex);
//    }

    /**
     * 获取joinPoint拦截的方法
     *
     * @param joinPoint
     * @return
     */
    private Method getMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazz = method.getParameterTypes();
                if (clazz.length == arguments.length) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 记录异常日志
     *
     * @param request
     * @param response
     * @param method
     * @param ex
     */
    public static void logException(HttpServletRequest request, HttpServletResponse response, Method method, Exception ex) {
        String credentials = response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
        String origin = response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        if (!response.isCommitted()) {
            response.reset();
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, must-revalidate");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, credentials);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,DELETE");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            if (ex instanceof ServiceException) {
                response.getWriter().write(JSON.toJSONString(BaseResult.no(ex.getMessage())));
                log.info("业务异常", ex);
            } else if(ex instanceof UnauthorizedException) {
                response.getWriter().write(JSON.toJSONString(BaseResult.no("未授权")));
            } else {
                Map<String, String> warnMsg = getErrorMsg(ex, request, method);
                response.getWriter().write(JSON.toJSONString(BaseResult.error(warnMsg.get("msg"))));
                log.error("系统错误", ex);
            }
        } catch (IOException e) {
            log.error("系统错误", e);
        }
    }

    /**
     * 记录请求日志，不包括异常
     *
     * @param request
     * @param response
     * @param method
     * @param result
     */
    public void logRequest(HttpServletRequest request, HttpServletResponse response, Method method, Object result) {
        StringBuilder sb = new StringBuilder(200);
        sb.append(NEW_LINE).append(getRequestInfoStr(request, method));
        if (result != null) {
            String msg = (result instanceof String) ? (String) result : JSON.toJSONString(result);
            sb.append("return: ").append(msg);
        }
        if (log.isInfoEnabled()) {
//            operationLogger.info(sb.toString(), method);
            log.info(sb.toString());
        }
    }

    /**
     * 获取请求相关信息String，如url, 参数
     *
     * @param request
     * @param method
     * @return
     */
    private static String getRequestInfoStr(HttpServletRequest request, Method method) {
        Map<String, Object> requestInfo = getRequestInfo(request, method);
        StringBuilder sb = new StringBuilder(200);
        for (Map.Entry<String, Object> entry : requestInfo.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(Objects.toString(entry.getValue(), StringUtils.EMPTY)).append(NEW_LINE);
        }
        return sb.toString();
    }

    /**
     * 获取请求相关信息map，如url, 参数
     *
     * @param request
     * @param method
     * @return
     */
    private static Map<String, Object> getRequestInfo(HttpServletRequest request, Method method) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("URI", request.getRequestURI());
        map.put("method", method.getDeclaringClass().getName() + "." + method.getName());
        map.put("params", getParamString(request.getParameterMap()));
        if (request.getAttribute(START_TIME) != null) {
            long duration = System.currentTimeMillis() - ((long) request.getAttribute(START_TIME));
            DecimalFormat df = new DecimalFormat("###,##0");
            map.put("duration", duration == 0 ? "-" : df.format(duration) + " ms");
        }
        return map;
    }

    /**
     * 获取请求参数信息
     *
     * @param map
     * @return
     */
    public static String getParamString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if (value != null && value.length == 1) {
                sb.append(value[0]).append("\t");
            } else {
                sb.append(Arrays.toString(value)).append("\t");
            }
        }
        return sb.toString();
    }

    /**
     * 获取异常message
     *
     * @param ex
     * @return
     */
    private static String getExceptionDefaultMsg(Exception ex) {
        String message;
        if (ExceptionUtils.getRootCause(ex) != null) {
            message = ExceptionUtils.getRootCause(ex).getMessage();
        } else {
            message = ex.getMessage();
        }
        return StringUtils.defaultIfBlank(message, ex.toString());
    }

    /**
     * 获取serviceexception信息
     *
     * @param ex
     * @param request
     * @param method
     * @return
     */
    private static Map<String, String> getWarnMsg(Exception ex, HttpServletRequest request, Method method) {
        Map<String, String> msgMap = new HashMap<>();
        String defaultMsg;
        if (ex instanceof ServiceException) {
            defaultMsg = ex.getMessage();
        } else {
            defaultMsg = getExceptionDefaultMsg(ex);
        }
        String msg = StringUtils.EMPTY;
        String msgLog = StringUtils.EMPTY;
        if (StringUtils.isBlank(msg)) msg = defaultMsg;
        if (StringUtils.isBlank(msgLog)) msgLog = defaultMsg;
        msgMap.put("msg", msg);
        msgMap.put("msgLog", msgLog);
        return msgMap;
    }

    /**
     * 获取error信息
     *
     * @param ex
     * @param request
     * @param method
     * @return
     */
    private static Map<String, String> getErrorMsg(Exception ex, HttpServletRequest request, Method method) {
        Map<String, String> msgMap = new HashMap<>();
        String defaultMsg = getExceptionDefaultMsg(ex);
        String msg = StringUtils.EMPTY;
        String msgLog = StringUtils.EMPTY;
        if (StringUtils.isBlank(msg)) msg = defaultMsg;
        if (StringUtils.isBlank(msgLog)) msgLog = defaultMsg;
        msgMap.put("msg", msg);
        msgMap.put("msgLog", msgLog);
        return msgMap;
    }

    /**
     * 替换占位字符
     *
     * @param str
     * @param user
     * @param method
     * @return
     */
    private String replaceStr(String str, String user, String method) {
        if (!StringUtils.isNotBlank(str)) return str;
        str = str.replaceAll(USER_EXP, user).replaceAll(METHOD_EXP, method);
        Object t = request.getAttribute(LOG_PARAMETER);
        if (t != null) {
            for (Map.Entry<String, String> entry : ((Map<String, String>) t).entrySet()) {
                str = str.replace(String.format(KEY_PARTTERN, entry.getKey()), entry.getValue());
            }
        }
        return str;
    }

    /**
     * 设置记录日志相关替换参数
     *
     * @param key
     * @param value
     */
    protected void setLogParameter(String key, String value) {
        Map<String, String> map = (Map<String, String>) request.getAttribute(LOG_PARAMETER);
        if (map == null) {
            map = new HashMap<>();
            request.setAttribute(LOG_PARAMETER, map);
        }
        map.put(key, value);
    }
}
