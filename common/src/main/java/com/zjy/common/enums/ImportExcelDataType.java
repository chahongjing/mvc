package com.zjy.common.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum ImportExcelDataType implements IBaseEnum {
    IMPORT_CORRECT_DATA(1, "当有异常数据时只导入正确的数据"),
    INTERUPT_WHEN_ERROR(2, "当有异常数据时不导入");

    private final int value;
    private final String name;

    ImportExcelDataType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
