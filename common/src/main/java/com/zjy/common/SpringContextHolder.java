package com.zjy.common;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author chahongjing
 * @create 2016-11-27 15:00
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, ServletContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null; // Spring应用上下文环境
    private static ServletContext servletContext = null;

    /**
     * @Description: 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param: applicationContext
     * @throws: BeansException
     * @CreateDate: 2016年6月26日
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * @Description: 取得存储在静态变量中的ApplicationContext(单例)
     * @return: ApplicationContext
     * @Author: <a href="mailto:unitysl@hotmail.com"/>沈浩</a>
     * @CreateDate: 2016年6月26日
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @Description: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param: name
     * @return: T 一个以所给名字注册的bean的实例
     * @throws: BeansException
     * @Author: <a href="mailto:unitysl@hotmail.com"/></a>
     * @CreateDate: 2016年6月26日
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        T bean = null;
        try {
            bean = (T) applicationContext.getBean(name);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return bean;
    }

    /**
     * @Description: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param: requiredType
     * @return: T
     * @throws: BeansException
     * @Author: <a href="mailto:unitysl@hotmail.com"/>沈浩</a>
     * @CreateDate: 2016年6月26日
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * @Description: 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param: name
     * @return: boolean
     * @Author: <a href="mailto:unitysl@hotmail.com"/>沈浩</a>
     * @CreateDate: 2016年6月26日
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * @Description: 判断以给定名字注册的bean定义是一个singleton还是一个prototype
     * @param: name
     * @return: boolean
     * @throws: NoSuchBeanDefinitionException
     * @Author: <a href="mailto:unitysl@hotmail.com"/>沈浩</a>
     * @CreateDate: 2016年6月26日
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        applicationContext = null;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }


    public void setServletContext(ServletContext servletContext) {
        SpringContextHolder.servletContext = servletContext;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static String getServletContextRealPath() {
        return servletContext.getRealPath("/");
    }
}