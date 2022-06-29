package com.zjy.baseframework.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String NEW_LINE = System.getProperty("line.separator", "\r\n");
    // 超级管理员
    public static Set<Long> SA_ADMIN = new HashSet<>(Arrays.asList(1L, 2L));
}
