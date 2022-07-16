package com.zjy.baseframework.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum ErrorCodeEnum implements IBaseEnum {
    UNAUTHENTICATION(1, "未登录"),
    UNAUTHORIZED(2, "未授权"),
    SERVICE_BUSY(3, "服务繁忙"),
    DUPLICATE_REQUEST(4, "重复请求"),
    DUPLICATE_NAME(5, "名称重复"),
    PARAM_ERROR(6, "参数错误"),
    PARAM_NULL(7, "参数不能为空");

    private int value;

    private String name;

    ErrorCodeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ErrorCodeEnum getByValue(int value) {
        return IBaseEnum.getByValue(ErrorCodeEnum.class, value);
    }
}

