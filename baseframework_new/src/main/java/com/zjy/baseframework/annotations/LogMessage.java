package com.zjy.baseframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMessage {
    /**
     * ServiceException异常信息，给用户看
     * @return
     */
    int warningMsg() default 0;
    /**
     * ServiceException异常信息，给用户看
     * @return
     */
    String warningMsgStr() default "";

    /**
     * ServiceException异常信息，给开发人员看
     * @return
     */
    int warningMsgLog() default 0;

    /**
     * ServiceException异常信息，给开发人员看
     * @return
     */
    String warningMsgLogStr() default "";

    /**
     * Exception异常信息，给用户看
     * @return
     */
    int errorMsg() default 0;

    /**
     * Exception异常信息，给用户看
     * @return
     */
    String errorMsgStr() default "";


    /**
     * Exception异常信息，给开发人员看
     * @return
     */
    int errorMsgLog() default 0;

    /**
     * Exception异常信息，给开发人员看
     * @return
     */
    String errorMsgLogStr() default "";

    /**
     * 操作成功的日志
     * @return
     */
    String successMsg() default "";

    /**
     * 是否记录日志
     * @return
     */
    boolean doLog() default true;
}
