package com.zjy.dao.common.multiDataSource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yehui
 * 自定义注解 + AOP的方式实现数据源动态切换。
 */
@Aspect
@Component
@Slf4j
@Order(-1)
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(com.zjy.dao.common.multiDataSource.DBSource)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        DataSourceKey dataSource = targetMethod.getAnnotation(DBSource.class).value();
        if (dataSource == null || dataSource == DataSourceKey.RANDOM) {
            dataSource = DynamicDataSourceContextHolder.getRandomDB();
        }
        //设置数据源
        DynamicDataSourceContextHolder.setDB(dataSource);
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            log.error("process dynamic data source error!", throwable);
            throw throwable;
        } finally {
            DynamicDataSourceContextHolder.removeDB();
        }
    }
}