package com.zjy.entity.enums;

import com.zjy.baseframework.annotations.MybatisFieldEnum;
import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;

/**
 * Created by Administrator on 2018/12/22.
 */
@MybatisFieldEnum
@SerializeEnum
public enum LogLevel implements IBaseEnum {
    ERROR(1, "Error"),
    WARN(2, "Warn"),
    INFO(3, "Info"),
    DEBUG(4, "Debug"),
    OTHER(0, "Other");

    final private int value;

    final private String name;

    LogLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static LogLevel getByName(String name) {
        for (LogLevel logLevel : LogLevel.values()) {
            if (logLevel.name().equalsIgnoreCase(name)) {
                return logLevel;
            }
        }
        return LogLevel.OTHER;
    }
}
