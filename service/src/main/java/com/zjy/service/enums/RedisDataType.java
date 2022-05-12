package com.zjy.service.enums;


import com.zjy.baseframework.interfaces.IBaseEnum;

public enum RedisDataType implements IBaseEnum {
    STRING(0, "字符串"),
    SET(1, "集合"),
    LIST(2, "列表"),
    HASH(3, "哈希"),
    ZSET(4, "有序集合");

    private final int value;
    private final String name;

    RedisDataType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
