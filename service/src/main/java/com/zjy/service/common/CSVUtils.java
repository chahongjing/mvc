package com.zjy.service.common;

import com.zjy.common.shiro.ShiroUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVUtils {
    static String[] header = new String[]{"id", "name", "code"};

    public static byte[] listToCsv(List<ShiroUserInfo> list) {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(stringWriter);
        CSVPrinter csvPrinter = initPrinter(bw);
        fillData(csvPrinter, list);
        return getWriterBytes(stringWriter);
    }

    public static CSVPrinter initPrinter(Appendable writer) {
        try {
            CSVPrinter printer = CSVFormat.EXCEL.print(writer);
            // 输出表头
            printer.printRecord(header);
            return printer;
        } catch (Exception e) {
            log.error("init error!", e);
        }
        return null;
    }

    public static void fillData(CSVPrinter printer, List<ShiroUserInfo> list) {
        List<Object> temp;
        try {
            for (ShiroUserInfo record : list) {
                temp = new ArrayList<>(header.length);
                temp.add(record.getId());
                temp.add(record.getName());
                temp.add(record.getCode());
                printer.printRecord(temp);
            }
            printer.flush();
        } catch (Exception e) {
            log.error("fillData error!", e);
        }
    }

    public static byte[] getWriterBytes(StringWriter stringWriter) {
        try {
            return stringWriter.toString().getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            log.error("getWriterBytes error!", e);
        }
        return null;
    }
}
