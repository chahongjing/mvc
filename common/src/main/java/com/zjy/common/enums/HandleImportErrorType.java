package com.zjy.common.enums;

import com.zjy.baseframework.interfaces.IBaseEnum;

public enum HandleImportErrorType implements IBaseEnum {
    RETURN_ERROR_MESSAGE(1, "返回错误信息"),
    DOWNLOAD_EXCEL_WITH_MESSAGE(2, "下载包含错误信息的excel");

    private final int value;
    private final String name;

    HandleImportErrorType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
