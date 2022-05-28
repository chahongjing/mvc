package com.zjy.baseframework.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum FileSuffix implements IBaseEnum {
    XLS(1, "xls"),
    XLSX(2, "xlsx"),
    DOC(3, "doc"),
    DOCX(4, "docx"),
    ZIP(5, "zip"),
    CSV(6, "csv");

    private int value;

    private String code;

    FileSuffix(int value, String code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static FileSuffix getByValue(int value) {
        return IBaseEnum.getByValue(FileSuffix.class, value);
    }
}
