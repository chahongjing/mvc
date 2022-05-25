package com.zjy.entity.model;


import com.zjy.baseframework.interfaces.IHierarchyBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Menu implements IHierarchyBase<Menu> {
    private Long id;
    private Long pid;
    private String name;
    private String code;
    private String url;
    private int seq;
    private String icon;

    @Override
    public void setChildren(List<Menu> children) {

    }
}
