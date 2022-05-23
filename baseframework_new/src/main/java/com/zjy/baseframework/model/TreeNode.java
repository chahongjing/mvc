package com.zjy.baseframework.model;

import com.zjy.baseframework.interfaces.IHierarchyBase;

import java.util.List;

public class TreeNode implements IHierarchyBase<TreeNode> {
    private Long id;
    private Long pid;
    private String name;
    private int seq;
    private boolean isLeaf;
    private boolean isOpen;
    private boolean selected;
    private Object data;
    private List<TreeNode> children;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
