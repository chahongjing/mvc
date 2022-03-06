package com.zjy.common.utils;

import com.zjy.baseframework.common.Constants;

import java.util.UUID;

public class Utils {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", Constants.EMPTY_STRING);
    }
}
