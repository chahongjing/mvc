package com.zjy.common.common;

/**
 * 日期
 * format参看ExcelHeader中的配置
 */
public class DateTimeExcelHeader extends ExcelHeader {

    public DateTimeExcelHeader(String fieldName) {
        this(fieldName, null);
    }

    public DateTimeExcelHeader(String fieldName, String name) {
        this(fieldName, name, "yyyy-MM-dd HH:mm:ss");
    }

    public DateTimeExcelHeader(String fieldName, String name, String format) {
        this(fieldName, name, format, -1);
    }

    public DateTimeExcelHeader(String fieldName, String name, String format, int columnIndex) {
        super(fieldName, name, format, columnIndex);
    }
}
