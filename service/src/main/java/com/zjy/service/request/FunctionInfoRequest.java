package com.zjy.service.request;

import com.zjy.service.common.PageInfomation;

public class FunctionInfoRequest extends PageInfomation {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}