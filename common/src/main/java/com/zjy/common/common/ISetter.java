package com.zjy.common.common;

import java.io.Serializable;

@FunctionalInterface
public interface ISetter<T, U> extends Serializable {
    void accept(T t, U u);
}