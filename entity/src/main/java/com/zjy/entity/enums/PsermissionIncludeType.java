package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
@SerializeEnum
public enum PsermissionIncludeType implements IBaseEnum {
    INCLUDE(0, "包含"),
    EXCLUDE(1, "排除");
    private final int value;
    private final String name;

    PsermissionIncludeType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}