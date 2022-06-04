package com.zjy.common.utils;

import java.io.Serializable;

@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}