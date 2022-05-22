package com.zjy.service.request;

import com.zjy.service.common.PageInfomation;

public class MenuRequest extends PageInfomation {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
