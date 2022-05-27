package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
@SerializeEnum
public enum MenuLevel implements IBaseEnum {
    FirstMenu(0, "一级菜单"),
    SecondMenu(1, "二级菜单");
    private int value;

    private String name;

    MenuLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }
    public static MenuLevel getByValue(int value) {
        return IBaseEnum.getByValue(MenuLevel.class, value);
    }
}
