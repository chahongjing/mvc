package com.zjy.common.utils;

import java.util.List;

/**
 * @author :
 * @Description: 多表头VO
 * @date Date : 2020年08月12日 14:43
 */

public class MultiHeaderVO {

    List<ExcelHeader> header;

    List data;

    public List<ExcelHeader> getHeader() {
        return header;
    }

    public void setHeader(List<ExcelHeader> header) {
        this.header = header;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public MultiHeaderVO(List<ExcelHeader> header, List data) {
        this.header = header;
        this.data = data;
    }

    public MultiHeaderVO() {
    }
}
