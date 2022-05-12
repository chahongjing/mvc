package com.zjy.service.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum RedisOpType implements IBaseEnum {
    GET(0, "获取"),
    SET(1, "设置（覆盖）"),
    DEL(2, "删除"),
    ADD_ITEM(3, "添加一项"),
    DEL_ITEM(4, "删除一项");

    private final int value;
    private final String name;

    RedisOpType(int value, String name) {
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
