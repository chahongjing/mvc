package com.zjy.service.component;

import com.zjy.service.common.PageBean;
import com.zjy.service.common.PageInfomation;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    int insert(T entity);
    int update(T entity);
    int delete(Long id);
    T get(Long id);
    List<? extends T> query(T entity);
    PageBean<? extends T> queryPage(PageInfomation pi, T entity);
    List<? extends T> queryListByMapFilter(Map<String, Object> query);
    PageBean<? extends T> queryPageListByMapFilter(PageInfomation pi, Map<String, Object> query);
}
