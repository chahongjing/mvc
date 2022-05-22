package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;

/**
 * Created by Administrator on 2018/12/26.
 */
@SerializeEnum
public enum OrderByType implements IBaseEnum {
    ASC(1, "升序"),
    DESC(2, "降序");

    private int value;
    private String name;

    OrderByType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
