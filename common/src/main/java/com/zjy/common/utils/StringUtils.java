package com.zjy.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String toUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append("\\u");
            String s = Integer.toHexString(str.charAt(i) & 0xffff);
            if(s.length() < 4) sb.append("0000".substring(0, 4 - s.length()));
            sb.append(s);
        }
        return sb.toString();
    }

    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            // group 6728
            String group = matcher.group(2);
            // ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            // group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }

    public static void main(String[] args) {
        String s = "a";
        Pattern compile = Pattern.compile("[\u4E00-\u9FA5]");
        Matcher matcher = compile.matcher(s);
        System.out.printf("find:%s %b%n", s, matcher.find());
        System.out.println(toUnicode(s));
        System.out.println(unicodeToString("\u4E00\u9FA5"));
    }
}
