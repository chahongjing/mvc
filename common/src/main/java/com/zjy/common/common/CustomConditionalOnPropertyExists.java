package com.zjy.common.common;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义condition注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(CustomOnPropertyExistsCondition.class)
public @interface CustomConditionalOnPropertyExists {

    /**
     * 配置的属性
     */
    String propName();

    /**
     * 是否存在
     */
    boolean exists() default true;
}