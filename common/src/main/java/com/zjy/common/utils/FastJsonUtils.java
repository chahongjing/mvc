package com.zjy.common.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.util.List;

public class FastJsonUtils implements JsonUtils{
    private FastJsonUtils() { }

    public <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public <T> List<T> parseList(String json, Class<T> tClass) {
        return JSON.parseArray(json, tClass);
    }

    public <T> T[] parseArray(String json, Class<T> tClass) {
        List<T> list = parseList(json, tClass);
        if(list == null) return null;
        T[] result = (T[]) Array.newInstance(tClass, list.size());
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public String toJSON(Object obj) {
        return JSON.toJSONString(obj);
    }
}
