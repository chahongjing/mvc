package com.zjy.service.common;

import com.alibaba.fastjson.annotation.JSONField;

public class EnumBean {
    @JSONField(ordinal = 1)
    private String key;
    @JSONField(ordinal = 2)
    private int value;
    @JSONField(ordinal = 3)
    private String code;
    @JSONField(ordinal = 4)
    private String name;
    @JSONField(ordinal = 5)
    private int order;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
