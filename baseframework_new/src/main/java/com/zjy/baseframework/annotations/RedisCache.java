package com.zjy.baseframework.annotations;

import com.zjy.baseframework.common.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    String key() default Constants.EMPTY_STRING;

    long expire() default 1;

    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
