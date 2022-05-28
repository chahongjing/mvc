package com.zjy.common.utils;

public class HyperlinkExcelHeader extends ExcelHeader {
    public HyperlinkExcelHeader(String fieldName) {
        this(fieldName, null);
    }

    public HyperlinkExcelHeader(String fieldName, String name) {
        this(fieldName, name, "link");
    }

    public HyperlinkExcelHeader(String fieldName, String name, int columnIndex) {
        this(fieldName, name, "link", columnIndex);
    }

    private HyperlinkExcelHeader(String fieldName, String name, String format) {
        this(fieldName, name, format, -1);
    }

    private HyperlinkExcelHeader(String fieldName, String name, String format, int columnIndex) {
        super(fieldName, name, format, columnIndex);
    }
}
