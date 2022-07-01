package com.zjy.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtils {
    // url group()
    public static Pattern urlReg = Pattern.compile("(http:|https:|ftp:)?//[^'\"\\s>]+");
    // img url: group(2)
    public static Pattern srcReg = Pattern.compile("<img[\\s]+?[^>]*?src=([\"'\\s>])?([^'\"\\s>]+).*?>");
    // base64 data: group(2), suffix: group(1)
    public static Pattern base64Reg = Pattern.compile("data:image/([^;]+);base64,([^'\"\\s>]+)");

    public static void main(String[] args) {
        try {
            String s = FileUtils.readFileToString(new File("c:/Users/zengjunyi/Desktop/t.txt"), StandardCharsets.UTF_8);
            Set<String> strings = parseUrl(s);
            for (String string : strings) {
                System.out.println(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> parseUrl(String content) {
        Set<String> set = new TreeSet<>();
        if(content == null || "".equals(content.trim())) return set;
        Matcher matcher = urlReg.matcher(content);
        while (matcher.find()) {
            set.add(matcher.group());
        }
        matcher = srcReg.matcher(content);
        while (matcher.find()) {
            set.add(matcher.group(2));
        }
        return set;
    }
}
