package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/11/13.
 */
@Getter
@SerializeEnum
public enum PermissionType implements IBaseEnum {
    FirstMenu(0, "一级菜单"),
    SecondMenu(1, "二级菜单"),
    FunctionItem(2, "功能"),
    Permission(3, "权限");

    private int value;

    private String name;

    PermissionType(int value, String name) {
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

    public static PermissionType getByValue(int value) {
        return IBaseEnum.getByValue(PermissionType.class, value);
    }
}
