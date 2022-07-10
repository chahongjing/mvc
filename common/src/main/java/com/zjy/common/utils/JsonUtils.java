package com.zjy.common.utils;

import java.util.List;

public interface JsonUtils {
    <T> T parse(String json, Class<T> clazz);

    <T> List<T> parseList(String json, Class<T> clazz);

    <T> T[] parseArray(String json, Class<T> clazz);

    String toJSON(Object obj);
}
