package com.zjy.dao.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface BaseDao<T> extends BaseMapper<T> {
    List<? extends T> query(T entity);
}
