package com.zjy.common;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
public class MyCustomZonedDateEditor extends PropertyEditorSupport {
    private static final List<DateTimeFormatter> dtf;
    protected static DateTimeFormatter utcDtf;
    protected static DateTimeFormatter gmtDtf;

    // LocalDateTime.now(ZoneId.of("UTC"));

    static {
        SimpleDateFormat sdf = (SimpleDateFormat) MyCustomDateEditor.getUtcSfd();
        String pattern = sdf.toPattern();
        utcDtf = DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC);

        sdf = (SimpleDateFormat) MyCustomDateEditor.getGmtSdf();
        pattern = sdf.toPattern();
        //gmtDtf = DateTimeFormatter.ofPattern(pattern.replace("'GMT'", StringUtils.EMPTY), Locale.US);
        gmtDtf = DateTimeFormatter.ofPattern(pattern, Locale.US).withZone(ZoneId.of("GMT"));
        dtf = new ArrayList<>();
        dtf.add(utcDtf);
        dtf.add(gmtDtf);
    }

    @Override
    public void setAsText(String text) {
        ZonedDateTime date = null;
        Date val = MyCustomDateEditor.parse(text);
        if (val != null) {
            // date = LocalDateTime.ofInstant(Instant.ofEpochSecond(val.toInstant().getEpochSecond()), ZoneOffset.UTC);
            date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(val.toInstant().getEpochSecond()), ZoneOffset.UTC);
        }
//        text = text.replaceAll("\\(.*\\)", StringUtils.EMPTY).trim() ;
//        for (DateTimeFormatter dateTimeFormat : dtf) {
//            try {
//                date = LocalDateTime.parse(text, dateTimeFormat);
//                break;
//            } catch (Exception e) {
//            }
//        }
        if (date == null) {
            log.error("解析Date失败！", text);
        }
        setValue(date);
    }
}
