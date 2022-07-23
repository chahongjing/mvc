package com.zjy.common.common;

import java.util.List;

public interface ExcelRowData {
    void appendErrorMsg(String errorMsg);
    List<String> getErrorMsg();
}
