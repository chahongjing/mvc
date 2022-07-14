package com.zjy.common.comparator;

import java.util.Comparator;
import java.util.TreeSet;

public class StringLengthComparator implements Comparator<String> {
    private StringLengthComparator() {
    }

    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        int length = s1.length() - s2.length();
        return length == 0 ? s1.compareTo(s2) : length;
    }

    public static void main(String[] args) {
        TreeSet<String> set = new TreeSet<>(new StringLengthComparator());
        set.add("ab");
        set.add("a");
        set.add(null);
        set.add("defa");
        set.add("efa");
        for (String s : set) {
            System.out.println(s);
        }
    }
}