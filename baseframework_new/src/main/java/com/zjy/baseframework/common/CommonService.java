package com.zjy.baseframework.common;

import com.zjy.baseframework.interfaces.IHierarchyBase;
import com.zjy.baseframework.interfaces.ISeq;
import com.zjy.baseframework.model.TreeNode;

import java.util.*;

public class CommonService {
    /**
     * 通过列表构建树
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends IHierarchyBase> List<T> buildTree(List<T> list) {
        // 先按父级分组
        Map<Long, List<T>> group = new HashMap<>();
        List<T> children;
        for (T node : list) {
            // 找到分组
            children = group.computeIfAbsent(node.getPid(), k -> new ArrayList<>());
            children.add(node);
        }
        // 树的一级列表
        List<T> firstLevelTreeNodeList = new ArrayList<>();
        // 开始构建树
        for (T node : list) {
            // 一级数据，是需要返回的
            if (node.getPid() == null || Constants.EMPTY_STRING.equals(node.getPid())) {
                firstLevelTreeNodeList.add(node);
            }
            // 设置当前节点的孩子节点
            children = group.computeIfAbsent(node.getId(), k -> new ArrayList<>());
            if (children.size() > 0) {
                // 子集排序
                children.sort(Comparator.comparing(IHierarchyBase::getSeq));
            }
            node.setChildren(children);
        }
        // 一级排序
        firstLevelTreeNodeList.sort(Comparator.comparing(IHierarchyBase::getSeq));
        return firstLevelTreeNodeList;
    }

    public static <T extends IHierarchyBase> boolean isRootNode(T node) {
        if (node == null) return false;
        return node.getPid() == null || node.getPid() == 0L;
    }

    public static <T extends ISeq> void exchange(T one, T two) {
        if (one == null || two == null) {
            return;
        }
        two.setSeq(one.getSeq() + two.getSeq());
        one.setSeq(two.getSeq() - one.getSeq());
        two.setSeq(two.getSeq() - one.getSeq());
    }

    public static void main(String[] args) {
        TreeNode one = new TreeNode();
        one.setId(1L);
        one.setName("1");
        one.setSeq(3);
        TreeNode two = new TreeNode();
        two.setId(2L);
        two.setName("2");
        two.setSeq(5);
        exchange(one, two);
        System.out.println(one.getSeq());
        System.out.println(two.getSeq());
    }
}
