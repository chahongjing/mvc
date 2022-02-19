package com.zjy.common.utils;

import com.zjy.entity.constant.Constants;

import java.util.UUID;

public class Utils {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", Constants.EMPTY_STRING);
    }
}
