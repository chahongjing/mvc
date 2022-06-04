package com.zjy.baseframework.common;

public class ExceptionUtils {
    /**
     * 获取异常message
     *
     * @param ex
     * @return
     */
    public static String getExceptionDefaultMsg(Throwable ex) {
        String message;
        if (org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(ex) != null) {
            message = org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(ex).getMessage();
        } else {
            message = ex.getMessage();
        }
        return message != null && message.length() > 0 ? message : ex.getMessage();
    }

    /**
     * 获取serviceexception信息
     *
     * @param ex
     * @return
     */
    public static String getWarnMsg(Throwable ex) {
        String defaultMsg;
        if (ex instanceof ServiceException) {
            defaultMsg = ex.getMessage();
        } else {
            defaultMsg = getExceptionDefaultMsg(ex);
        }
        return defaultMsg;
    }

    /**
     * 获取error信息
     *
     * @param ex
     * @return
     */
    public static String getErrorMsg(Throwable ex) {
        return getExceptionDefaultMsg(ex);
    }
}
