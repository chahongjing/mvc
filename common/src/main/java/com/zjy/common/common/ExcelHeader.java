package com.zjy.common.common;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author
 * @date 2019-12-12 22:24:53
 */
public class ExcelHeader {
    private String fieldName;
    private String name;
    /**
     * 格式：
     * 长整型数字：###0
     * 小数和金额：¥#,##0.00，注意：¥为前导字符
     * 百分比：#,##0.00%，注意：%会在数字基础上自动*100
     * 日期：yyyy-MM-dd HH:mm:ss
     */
    private String format;
    private int columnIndex;
    private CellStyle cellStyle;
    private CellStyle headerCellStyle;
    private Hyperlink hyperlink;

    public ExcelHeader(String fieldName) {
        this(fieldName, null);
    }

    public ExcelHeader(String fieldName, String name) {
        this(fieldName, name, null);
    }

    /**
     * @param fieldName
     * @param name
     * @param format
     * @see DateTimeExcelHeader  时间格式
     * @see NumberExcelHeader  数字格式
     * @deprecated
     */
    @Deprecated
    public ExcelHeader(String fieldName, String name, String format) {
        this(fieldName, name, format, -1);
    }

    public ExcelHeader(String fieldName, String name, String format, int columnIndex) {
        this.fieldName = fieldName;
        this.name = (name != null && !name.trim().equals("")) ? name : fieldName;
        this.format = format;
        this.columnIndex = columnIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public CellStyle getHeaderCellStyle() {
        return headerCellStyle;
    }

    public void setHeaderCellStyle(CellStyle headerCellStyle) {
        this.headerCellStyle = headerCellStyle;
    }

    public Hyperlink getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }
}
