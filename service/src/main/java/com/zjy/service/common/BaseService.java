package com.zjy.service.common;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    int insert(T entity);
    int update(T entity);
    int delete(Long id);
    T get(Long id);
    List<? extends T> queryList(T entity);
    PageBean<? extends T> queryPageList(PageInfomation pi, T entity);
    List<? extends T> queryListByMapFilter(Map<String, Object> query);
    PageBean<? extends T> queryPageListByMapFilter(PageInfomation pi, Map<String, Object> query);
}
