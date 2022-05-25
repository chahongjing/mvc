package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
@SerializeEnum
public enum UserTypeEnum implements IBaseEnum {
    SUPER_ADMIN(0, "超级管理员"),
    NORMAL(1, "普通用户");
    private final int value;
    private final String name;

    UserTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}