package com.zjy.service.enums;

import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.enums.ResultStatus;
import com.zjy.baseframework.interfaces.IBaseEnum;

public enum SwitchEnum implements IBaseEnum {
    SWITCH_ONE(1, "第一个开关"),
    SWITCH_TWO(2, "第二个开关");

    private final int value;
    private final String name;
    public static final String SWITCH_KEY = RedisKeyUtils.SWITCH_KEY;
    public static final String SWITCH_OPEN = "1";
    public static final String SWITCH_CLOSE = "0";


    SwitchEnum(int value, String name) {
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

    public static SwitchEnum getByValue(int value) {
        return IBaseEnum.getByValue(SwitchEnum.class, value);
    }
}
