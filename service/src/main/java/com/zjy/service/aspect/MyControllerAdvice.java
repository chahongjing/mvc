package com.zjy.service.aspect;

import com.zjy.baseframework.common.DownloadException;
import com.zjy.baseframework.common.ExceptionUtils;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@ControllerAdvice
//@RestControllerAdvice(basePackages = {"com.bugpool.leilema"})   implements ResponseBodyAdvice<Object>
public class MyControllerAdvice {
    @Autowired
    private JsonUtils jsonUtils;

    @ExceptionHandler
    public void processException(HttpServletRequest request, HttpServletResponse response, Exception ex, HandlerMethod handler) {
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
            String msg = ExceptionUtils.getWarnMsg(ex);
            PrintWriter writer = response.getWriter();
            if (ex instanceof ServiceException) {
                writer.write(jsonUtils.toJSON(BaseResult.no(msg)));
            } else if (ex instanceof DownloadException) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                writer.write(jsonUtils.toJSON(BaseResult.no(msg)));
            } else if (ex instanceof UnauthorizedException) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else if (ex instanceof UnauthenticatedException) {
                response.setStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            } else {
                writer.write(jsonUtils.toJSON(BaseResult.error(ExceptionUtils.getErrorMsg(ex))));
            }
        } catch (IOException e) {
            log.error("系统错误", e);
        }
    }
}
