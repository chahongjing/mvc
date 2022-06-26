package com.zjy.baseframework.interfaces;

import java.util.List;

/**
 * Created by jyzeng on 2018/3/23.
 * 层级关系实体接口
 */
public interface IHierarchyBase<T extends IHierarchyBase> {
    Long getId();
    Long getPid();
    int getSeq();
    void setSeq(int seq);
    void setChildren(List<T> children);
}
