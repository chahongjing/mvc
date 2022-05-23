package com.zjy.baseframework.interfaces;

import java.util.Map;

/**
 * Created by chahongjing on 2018/1/27.
 */
public interface ICache {
    Object get(String key);
    <T> T get(String key, Class<T> clazz);
    <T> void set(String key, T value);
    boolean delete(String key);
    Map<String, Object> getAll(String key);
//    Object hGet(String key, String field);
//    long hSet(String key, String field, String value);
//    long hDelete(String key);
//    long hDelete(String key, String field);
}
