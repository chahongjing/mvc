package com.zjy.baseframework.enums;

import com.zjy.baseframework.annotations.MybatisFieldEnum;
import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;

@SerializeEnum
@MybatisFieldEnum
public enum YesNo implements IBaseEnum {
    NO(0, "否", 2),
    YES(1, "是", 1);

    private int value;

    private String name;

    private int order;

    YesNo(int value, String name, int order) {
        this.value = value;
        this.name = name;
        this.order = order;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public static YesNo getByValue(int value) {
        return IBaseEnum.getByValue(YesNo.class, value);
    }
}
