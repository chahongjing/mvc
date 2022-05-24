package com.zjy.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum Sex2 implements IEnum<Integer> {
    MALE(0, "男"),
    FEMALE(1, "女")
    ;
    private final Integer value;
    private final String name;

    Sex2(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
