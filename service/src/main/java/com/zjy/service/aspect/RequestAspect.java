package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.LogMessage;
import com.zjy.baseframework.common.Constants;
import com.zjy.baseframework.common.DownloadException;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.common.shiro.IUserInfo;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 记录请求信息，包括入参，返回值，异常，发送飞书报警
 * 执行顺序
 * 1. 正常执行：beforeAround --> before --> process --> afterReturning --> after --> afterAround
 * 2. 异常执行：beforeAround --> before --> process --> afterThrowing --> after
 *
 * @author
 * @date 2020-11-23 21:25:25
 */
@Slf4j
@Aspect
@Order(100)
@Component
public class RequestAspect {
    // region 属性
    // 请求信息，用于web相关项目
    private HttpServletRequest request;

    @Autowired
    private JsonUtils jsonUtils;

    private static Logger dbLogger = LoggerFactory.getLogger("dbLogger");

    public String getUserId() {
        IUserInfo currentUser = ShiroRealmUtils.getCurrentUser();
        if(currentUser != null && currentUser.getId() != null) {
            return currentUser.getId().toString();
        }
        return "";
    }
    // endregion

    // region aop
    /**
     * 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(com.zjy.baseframework.annotations.LogMethod) " +
//            "|| execution(* com.zjy.service.service.impl..*.*(..)) " +
            "")
    public void cstAspect() {
    }

    @Around("cstAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        request = getRequest();
        Object object;
        Method method = getMethod(joinPoint);
        LogMessage annotation = method.getAnnotation(LogMessage.class);

        StringBuilder msg = new StringBuilder();
        msg.append(String.format("用户【%s】请求", getUserId()));
        if(isController(request, method)) {
            msg.append(String.format("url【%s】,", request.getRequestURI()));
        }
        msg.append(String.format("方法【%s】", getRequestMethodStr(method)));
        String requestPamramStr = getRequestParamStr(method, joinPoint, request);
        if(annotation == null || annotation.doLog()) {
            log.info("{}。请求参数：{}", msg.toString(), requestPamramStr);
        }
        long begin = System.currentTimeMillis();
        try {
            object = joinPoint.proceed();
            if(annotation == null || annotation.doLog()) {
                log.info("完成返回【{}】。{}。结果：{}", getTimeSpan(begin), msg.toString(), jsonUtils.toJSON(object));
            }
            dbLogger.info("完成返回【{}】。{}。结果：{}", getTimeSpan(begin), msg.toString(), jsonUtils.toJSON(object), method);
        } catch (Throwable ex) {
            if(ex instanceof ServiceException || ex instanceof DownloadException) {
                log.warn("完成返回【{}】。{}。结果：业务提示", getTimeSpan(begin), msg.toString(), ex);
                dbLogger.warn(msg.toString(), method);
            } else if(ex instanceof UnauthorizedException || ex instanceof UnauthenticatedException) {
            } else {
                log.info("完成返回【{}】。{}。结果：业务异常", getTimeSpan(begin), msg.toString(), ex);
                dbLogger.error(msg.toString(), method);
            }
            throw ex;
        }
        return object;
    }

//    @AfterReturning(pointcut = "cstAspect()", returning = "ret")
//    public void afterReturning(JoinPoint joinPoint, Object ret) {
//    }
//
//    @AfterThrowing(pointcut = "cstAspect()", throwing = "ex")
//    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
//    }
    // endregion

    // region 辅助方法

    /**
     * 获取当前方法
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
     * 获取参数字符串信息
     *
     * @param method
     * @param param
     * @return
     */
    private String getParameter(Method method, Object[] param) {
        StringBuilder res = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {
            String v;
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getType() == HttpServletRequest.class || parameters[i].getType() == HttpServletResponse.class) {
                    continue;
                }
                if (param[i] instanceof String) {
                    v = (String) param[i];
                } else if (param[i] != null) {
                    v = jsonUtils.toJSON(param[i]);
                } else {
                    v = "";
                }
                res.append(String.format(parameters[i].getName() + "=" + v + "\t"));
            }
        }
        return res.toString();
    }

    /**
     * 格式化时间
     *
     * @param before
     * @return
     */
    private String getTimeSpan(long before) {
        long duration = System.currentTimeMillis() - before;
        DecimalFormat df = new DecimalFormat("###,##0");
        return duration == 0 ? "-" : df.format(duration) + " ms";
    }

    /**
     * 记录请求信息
     *
     * @param request
     * @param method
     */
    private void logRequest(HttpServletRequest request, Method method) {
        StringBuilder sb = new StringBuilder(200);
        sb.append(Constants.NEW_LINE).append(getRequestInfoStr(request, method).trim());
        log.info(sb.toString());
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
            sb.append(entry.getKey()).append(": ").append(Objects.toString(entry.getValue(), StringUtils.EMPTY)).append(Constants.NEW_LINE);
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
        map.put("http请求URI", request.getRequestURI() + "\t\tmethod：" + method.getDeclaringClass().getName() + "." + method.getName());
        map.put("params", getParamString(request.getParameterMap()));
        return map;
    }

    /**
     * 获取请求参数信息
     *
     * @param map
     * @return
     */
    private static String getParamString(Map<String, String[]> map) {
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
     * 发送飞书报警
     *
     * @param time
     * @param ex
     */
    private void sendLarkMessage(String time, String reqMethod, String param, Throwable ex) {
//        String msg = String.format("%s 失败 返回【%s】。%s原因：%s", reqMethod, time, Constants.NEW_LINE, ExceptionUtils.getWarnMsg(ex));
//        if (ex instanceof ServiceException || ex instanceof IllegalArgumentException) {
//            log.warn(msg, ex);
//            try {
//                Monitor.alarm(Constants.MONITOR_KEY, "【" + getType() + "】" + ex.getMessage() + Constants.NEW_LINE + param);
//            } catch (Exception e) {
//                log.error("发送飞书报警异常！", e);
//            }
//        } else if (ex instanceof DownloadException) {
//            log.warn(msg, ex);
//        } else if (ex instanceof UnauthorizedException) {
//            log.info("用户未授权！", ex);
//        } else if (ex instanceof UnauthenticatedException) {
//            log.info("用户未登录！", ex);
//        } else {
//            log.error(msg, ex);
//            try {
//                Monitor.alarm(Constants.MONITOR_KEY, reqMethod + "失败！参数：" + param + Constants.NEW_LINE + "原因：" + ExceptionUtils.getWarnMsg(ex));
//            } catch (Exception e) {
//                log.error("发送飞书报警异常！", e);
//            }
//        }
    }

    private String getRequestMethodStr(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    private String getRequestParamStr(Method method, ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        if(isController(request, method)) {
            String paramString;
            try {
                paramString = getParamString(request.getParameterMap());
            } catch (Exception ex) {
                paramString = getParameter(method, joinPoint.getArgs());
            }
            return paramString;
        } else {
            return getParameter(method, joinPoint.getArgs());
        }
    }

    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return servletRequestAttributes.getRequest();
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isController(HttpServletRequest request, Method method) {
        return request != null && method.getDeclaringClass().getPackage().getName().endsWith("controller");
    }
    // endregion
}
