package com.zjy.baseframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitByCount {
    // 限制次数，最多允许5个线程，超过expire()，即60秒自动删除key
    int count() default 5;

    // 过期时间，单位秒
    int expire() default 60;

    // 生成的key是否包含用户信息
    boolean withUser() default false;

    // 生成的key是否包含参数信息
    boolean withParam() default false;

    // 调用完成是否自动减count
    boolean autoDecr() default true;
}