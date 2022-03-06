package com.zjy.baseframework.interfaces;

import java.util.List;

/**
 * Created by jyzeng on 2018/3/23.
 * 层级关系实体接口
 */
public interface IHierarchyBase<T extends IHierarchyBase> {
    String getId();
    String getPId();
    int getSeq();
    void setChildren(List<T> children);
}
