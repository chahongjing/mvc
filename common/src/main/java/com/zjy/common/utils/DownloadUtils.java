package com.zjy.common.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by chahongjing on 2017/2/14.
 */
public class DownloadUtils {
    //    protected static Logger logger = LoggerFactory.getLogger(DownloadUtils.class);
    private DownloadUtils() {
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing

        return bytes;
    }

    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }
        return bytes;
    }


    public static void download(String filePath, HttpServletResponse response) throws IOException {
        download(filePath, response, "");
    }

    public static void download(String filePath, HttpServletResponse response, String fileName) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        download(file, response, fileName);
    }

    public static void download(File file, HttpServletResponse response) throws IOException {
        download(file, response, file.getName());
    }

    public static void download(File file, HttpServletResponse response, String fileName) throws IOException {
        if (!file.exists()) throw new FileNotFoundException("未找到文件" + file);
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)) {
            download(bis, (fileName == null || fileName.trim().equals("")) ? file.getName() : fileName, response);
        }
    }

    public static void download(InputStream is, String fileName, HttpServletResponse response) throws IOException {
        download(IOUtils.toByteArray(is), fileName, response);
    }

    public static void download(byte[] bytes, String fileName, HttpServletResponse response) throws IOException {
        if (fileName == null || fileName.trim().equals("")) throw new IllegalArgumentException("文件名称不能为空！");
        // 创建输出流
        try (OutputStream out = response.getOutputStream()) {
            resetResponse(response);
            // 设置响应编码
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // 设置文件名
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())));
            // 设置contentType
            response.setContentType(FileUtils.getMimeType(new File(fileName)));
            // 数据信息写入响应流中
            IOUtils.write(bytes, out);
            // 关闭文件输入流
            out.flush();
        } catch (IOException ex) {
//            logger.error("下载失败!", ex);
            throw ex;
        }
    }

    public static void resetResponse(HttpServletResponse response) {
        resetResponse(response, 200);
    }

    public static void resetResponse(HttpServletResponse response, int httpStatus) {
        String credentials = response.getHeader("Access-Control-Allow-Credentials");
        String origin = response.getHeader("Access-Control-Allow-Origin");
        // 清空response
        if (!response.isCommitted()) {
            response.reset();
        }
        response.setStatus(httpStatus);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, must-revalidate");
        response.setHeader("Access-Control-Allow-Credentials", credentials);
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setHeader("Access-Control-Expose-Headers", HttpHeaders.CONTENT_DISPOSITION);
        // 设置响应编码
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
