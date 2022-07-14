package com.zjy.common.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    // phone
    public static Pattern phoneReg = Pattern.compile("^((0\\d{10,11})|(1\\d{10}))$");
    public static Pattern phoneReg1 = Pattern.compile("^((1[0-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public static Pattern p = Pattern.compile("([\u4E00-\u9FA5【】（）！￥…·、《》？，。]+)");

    public static void main(String[] args) {
//        try {
//            String s = FileUtils.readFileToString(new File("c:/Users/zengjunyi/Desktop/t.txt"), StandardCharsets.UTF_8);
//            Set<String> strings = parseUrl(s);
//            for (String string : strings) {
//                System.out.println(string);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            String url = "https://my.net/kbs/9f868108-a634-40a5-b6a7-31becf12447l_-1645066060367%20(2)-1654566788261.webp?GalaxyAccessKeyId=AKLDHO7N6X4PBYAVAA&amp;Expires=1659283200000&amp;Signature=z2fCbB77zt7YdeRd/KUt8Qsg/p8=";
            URI uri = new URI(url);
            String path = uri.getPath();
            if (path != null && !"".equals(path.trim())) {
                String[] arr = path.split("/");
                String fileName = arr[arr.length - 1];
                fileName = URLEncoder.encode(URLDecoder.decode(fileName, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
                arr[arr.length - 1] = fileName;
                String join = String.join("/", arr);
                String m = String.format("%s://%s%s%s?%s", uri.getScheme(), uri.getHost(), uri.getPort() == -1 ? "" : (":" + uri.getPort()), join, uri.getQuery());
                System.out.println("a:" + url);
                System.out.println("b:" + m);
            }
            String rawPath = uri.getRawPath();
            Matcher m = p.matcher(url);
//        String $1 = m.replaceAll("($1)");
            while (m.find()) {
                url = url.replace(m.group(0), URLEncoder.encode(m.group(0), StandardCharsets.UTF_8.name()));
            }
            System.out.println("c:" + url);
        } catch (Exception ignore) {

        }
    }

    public static Set<String> parseUrl(String content) {
        Set<String> set = new TreeSet<>();
        if (content == null || "".equals(content.trim())) return set;
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
