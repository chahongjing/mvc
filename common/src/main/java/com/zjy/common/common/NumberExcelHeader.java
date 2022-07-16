package com.zjy.common.common;

/**
 * 数字格式，长整型数，小数，百分比，金额
 * format参看ExcelHeader中的配置
 */
public class NumberExcelHeader extends ExcelHeader {
    public NumberExcelHeader(String fieldName) {
        this(fieldName, null);
    }

    public NumberExcelHeader(String fieldName, String name) {
        this(fieldName, name, "###0");
    }

    public NumberExcelHeader(String fieldName, String name, String format) {
        this(fieldName, name, format, -1);
    }

    public NumberExcelHeader(String fieldName, String name, String format, int columnIndex) {
        super(fieldName, name, format, columnIndex);
    }
}
