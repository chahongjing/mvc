package com.zjy.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtils {
    // url group()
    private static Pattern urlReg = Pattern.compile("(http:|https:|ftp:)?//[^'\"\\s>]+");
    // img group(2)
    private static Pattern srcReg = Pattern.compile("<img[\\s]+?[^>]*?src=([\"'\\s>])?([^'\"\\s>]+).*?>");
    // base64 group(1)
    private static Pattern base64Reg = Pattern.compile("data:image/[^;]+;base64,([^'\"\\s>]+)");

    public static void main(String[] args) {
        try {
            String s = FileUtils.readFileToString(new File("c:/Users/zengjunyi/Desktop/t.txt"), StandardCharsets.UTF_8);
            Matcher matcher = srcReg.matcher(s);
            while(matcher.find()) {
                System.out.println(matcher.group());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
