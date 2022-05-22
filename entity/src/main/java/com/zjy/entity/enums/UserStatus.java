package com.zjy.entity.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
public enum UserStatus implements IBaseEnum {
    NORMAL(0, "正常"),
    DELETED(1, "已删除")
    ;
    private final int value;
    private final String name;

    UserStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
