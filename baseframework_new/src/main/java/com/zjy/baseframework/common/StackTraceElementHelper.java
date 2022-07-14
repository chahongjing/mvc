package com.zjy.baseframework.common;

/**
 * Created by Administrator on 2018/12/22.
 */
public class StackTraceElementHelper {
    private StackTraceElementHelper() {
    }

    public static StackTraceElement extractFirstCaller(StackTraceElement[] callerDataArray) {
        return hasAtLeastOneNonNullElement(callerDataArray) ? callerDataArray[0] : null;
    }

    private static boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
        return callerDataArray != null && callerDataArray.length > 0 && callerDataArray[0] != null;
    }
}
