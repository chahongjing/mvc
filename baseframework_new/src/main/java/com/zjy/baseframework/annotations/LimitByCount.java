package com.zjy.baseframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitByCount {
    // 限制次数
    int count() default 5;
    // 生成的key是否包含用户信息
    boolean withUser() default false;
    // 生成的key是否包含参数信息
    boolean withParam() default false;
}