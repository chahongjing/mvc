package com.zjy.common.common;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 自定义condition注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(CustomOnPropertyHasValueCondition.class)
public @interface CustomConditionalOnPropertyHasValue {

    /**
     * 配置的属性
     */
    String propName();

    /**
     * 是否存在
     */
    boolean hasValue() default true;
}