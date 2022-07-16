package com.zjy.baseframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁止重复操作
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatOp {
    // 时间内不允许重复操作，如10秒内只能操作一次
    int timeout() default 10;

    // 生成的key是否包含用户信息
    boolean withUser() default true;

    // 生成的key是否包含参数信息
    boolean withParam() default true;

    // 请求完成后是否删除key
    boolean del() default true;
}
