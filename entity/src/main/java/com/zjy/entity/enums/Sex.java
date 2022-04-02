package com.zjy.entity.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
public enum Sex implements IBaseEnum {
    MALE(0, "男"),
    FEMAIL(1, "女")
    ;
    private final int value;
    private final String name;

    Sex(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
