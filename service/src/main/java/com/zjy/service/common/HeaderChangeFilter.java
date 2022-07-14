package com.zjy.service.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * junyi.zeng
 */
//@Component
public class HeaderChangeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new ChangeRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

    class ChangeRequestWrapper extends HttpServletRequestWrapper {
        private static final String ACCEPT = "Accept";
        private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

        public ChangeRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if (ACCEPT.equalsIgnoreCase(name)) {
                return APPLICATION_JSON_CHARSET_UTF_8;
            }
            return super.getHeader(name);
        }
    }
}