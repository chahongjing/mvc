package com.zjy.baseframework.common;

import com.zjy.baseframework.interfaces.IHierarchyBase;

import java.util.*;

public class CommonService {
    /**
     * 通过列表构建树
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends IHierarchyBase> List<T> buildTree(List<T> list) {
        // 先按父级分组
        Map<String, List<T>> group = new HashMap<>();
        List<T> children;
        for (T node : list) {
            // 找到分组
            children = group.computeIfAbsent(node.getPId(), k -> new ArrayList<>());
            children.add(node);
        }
        // 树的一级列表
        List<T> firstLevelTreeNodeList = new ArrayList<>();
        // 开始构建树
        for (T node : list) {
            // 一级数据，是需要返回的
            if (node.getPId() == null || Constants.EMPTY_STRING.equals(node.getPId())){
                firstLevelTreeNodeList.add(node);
            }
            // 设置当前节点的孩子节点
            children = group.computeIfAbsent(node.getId(), k -> new ArrayList<>());
            if(children.size() > 0) {
                // 子集排序
                children.sort(Comparator.comparing(IHierarchyBase::getSeq));
            }
            node.setChildren(children);
        }
        // 一级排序
        firstLevelTreeNodeList.sort(Comparator.comparing(IHierarchyBase::getSeq));
        return firstLevelTreeNodeList;
    }
}
