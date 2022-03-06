package com.zjy.baseframework.interfaces;

import com.zjy.baseframework.common.Constants;

/**
 * Created by Administrator on 2018/5/15.
 */
public interface IBaseEnum {
    default int getValue() {
        throw new UnsupportedOperationException("未实现getValue方法");
    }

    default String getCode() {
        return Constants.EMPTY_STRING;
    }

    default String getName() {
        return Constants.EMPTY_STRING;
    }

    default int getOrder() {
        return 0;
    }

    static <E extends Enum<E> & IBaseEnum> E getByValue(Class<E> enumClass, Integer value) {
        if (value == null) return null;
        E[] enumConstants = enumClass.getEnumConstants();
        for (E item : enumConstants) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        return null;
    }

    static <E extends Enum<E> & IBaseEnum> E getByCode(Class<E> enumClass, String code) {
        if (code == null || Constants.EMPTY_STRING.equals(code.trim())) return null;
        E[] enumConstants = enumClass.getEnumConstants();
        for (E item : enumConstants) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }
}