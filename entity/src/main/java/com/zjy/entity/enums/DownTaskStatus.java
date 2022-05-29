package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
@SerializeEnum
public enum DownTaskStatus implements IBaseEnum {
    CREATED(0, "已创建"),
    STARTED(1, "处理中"),
    FINISHED(2, "已完成"),
    ERROR(9, "错误");

    private final int value;
    private final String name;

    DownTaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
