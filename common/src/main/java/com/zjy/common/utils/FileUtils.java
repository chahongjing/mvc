package com.zjy.common.utils;

import org.springframework.util.MimeTypeUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    private static MimetypesFileTypeMap map = new MimetypesFileTypeMap();
    public static String getMimeType(String file) {
        if(file == null || "".equals(file.trim())) return MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
        return getMimeType(new File(file));
    }
    public static String getMimeType(File file) {

        String mimeType = null;
        try {
            mimeType = Files.probeContentType(file.toPath());
        } catch (IOException ignored) {
        }
        if(mimeType != null && !"".equals(mimeType)) return mimeType;

        mimeType = map.getContentType(file);
        if(mimeType != null && !"".equals(mimeType)) return mimeType;

//        Collection mimeTypes = MimeUtil.getMimeTypes(file);
//        if(mimeTypes != null && mimeTypes.size() > 0) {
//            mimeType = mimeTypes.toString();
//        }
        return mimeType;
    }

    public static void main(String[] args) {
        String[] s = new String[]{".txt", ".log", ".jpg", ".jpeg", ".png", ".html",
                ".js", ".css", ".zip", ".rar", "abc", ".xls", ".xlsx", ".doc", ".docx", ".ppt", ".pptx",
        ".gz", ".aac", ".mp3", ".mp4", ".wav", ".avi", ".bin", ".csv", ".gif", ".jar",".pdf",
        ".svg",".tar",".woff",".xml",".3gp","7z", ".json"};
        for (String s1 : s) {
            String mimeType = getMimeType(s1);
            System.out.println(s1 + ": " + mimeType);
        }
    }
}
