package com.zjy.common.utils;

import java.io.Serializable;

@FunctionalInterface
public interface ISetter<T, U> extends Serializable {
    void accept(T t, U u);
}