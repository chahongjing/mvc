package com.zjy.baseframework.common;

import com.zjy.baseframework.interfaces.IHierarchyBase;
import com.zjy.baseframework.interfaces.ISeq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonService {
    /**
     * 通过列表构建树
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends IHierarchyBase<T>> List<T> buildTree(List<T> list) {
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
            if (isRootNode(node)) {
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

    public static <T extends IHierarchyBase<T>> boolean isRootNode(T node) {
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

    /**
     * 取交集
     * @param lists
     * @param <T>
     * @return
     */
    public static <T> Collection<T> retainAll(Collection<T>... lists) {
        if(lists == null || lists.length == 0) {
            return new ArrayList<>();
        } else if(lists.length == 1) {
            return lists[0];
        }
        long minSize = Long.MAX_VALUE;
        Collection<T> min = lists[0];
        List<Set<T>> oSet = new ArrayList<>();
        for (Collection<T> ts : lists) {
            if(ts.size() < minSize) {
                minSize = ts.size();
                min = ts;
            }
        }
        for (Collection<T> ts : lists) {
            if(ts != min) {
                oSet.add(new HashSet<>(ts));
            }
        }
        boolean allContains;
        Iterator<T> iterator = min.iterator();
        List<T> target = new ArrayList<>((int)minSize);
        while (iterator.hasNext()) {
            T t = iterator.next();
            allContains = true;
            for (Set<T> ts : oSet) {
                if(!ts.contains(t)) {
                    allContains = false;
                    break;
                }
            }
            if (allContains) target.add(t);
        }
        return target;
    }

    public static void main(String[] args) {
//        TreeNode one = new TreeNode();
//        one.setId(1L);
//        one.setName("1");
//        one.setSeq(3);
//        TreeNode two = new TreeNode();
//        two.setId(2L);
//        two.setName("2");
//        two.setSeq(5);
//        exchange(one, two);
//        System.out.println(one.getSeq());
//        System.out.println(two.getSeq());

        List<Long> l1 = new ArrayList<>();
        List<Long> l2 = new ArrayList<>();
        List<Long> l3 = new ArrayList<>();
        int num = 100000;
        for(int i = 0; i < num; i++) {
            l1.add((long)(Math.random() * num));
        }
        for(int i = 0; i < num + 1; i++) {
            l2.add((long)(Math.random() * num));
        }
        for(int i = 0; i < num - 1; i++) {
            l3.add((long)(Math.random() * num));
        }
//        System.out.println("l1");
//        for (Long aLong : l1) {
//            System.out.printf("%d\t", aLong);
//        }
//        System.out.println("");
//        System.out.println("l2");
//        for (Long aLong : l2) {
//            System.out.printf("%d\t", aLong);
//        }
//        System.out.println("");
//        System.out.println("l3");
//        for (Long aLong : l3) {
//            System.out.printf("%d\t", aLong);
//        }
        long t = System.currentTimeMillis();
        Collection<Long> ts1 = retainAll(l1, l2, l3);
        System.out.printf("%ntime: %d%n", System.currentTimeMillis() - t);
//        System.out.println("res");
//        for (Long aLong : ts) {
//            System.out.printf("%d\t", aLong);
//        }
    }
}
