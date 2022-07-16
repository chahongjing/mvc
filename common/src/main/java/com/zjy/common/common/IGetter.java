package com.zjy.common.common;

import java.io.Serializable;

@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}