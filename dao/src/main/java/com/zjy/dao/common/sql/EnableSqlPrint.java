package com.zjy.dao.common.sql;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SqlConfig.class)
@Documented
public @interface EnableSqlPrint {

    /**
     * 是否默认打sql, 优先级最低,
     * 可以通过和SqlLog注解组合打印sql
     * sqlLog具有最高优先级
     */
    boolean defaultPrint() default true;

}