package com.zjy.entity.model;


import com.zjy.baseframework.interfaces.IHierarchyBase;

import java.util.List;

public class Menu implements IHierarchyBase<Menu> {
    private Long id;
    private Long pId;
    private String name;
    private String code;
    private String url;
    private int seq;
    private String icon;

    private List<Menu> children;

    public Long getMenuId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPId() {
        return pId;
    }

    public void setPId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Menu> getChildren() {
        return children;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setChildren(List<Menu> children) {

    }
}
