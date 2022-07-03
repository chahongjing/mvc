package com.zjy.common.common;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

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