package com.zjy.common.stratory;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum EventHandlerType implements IBaseEnum {
    CREATE(1, "创建"),
    CLOSE(2, "关闭");

    private int value;

    private String name;

    EventHandlerType(int value, String name) {
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
}
