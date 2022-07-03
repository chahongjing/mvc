package com.zjy.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * @author
 * @date 2019-12-03 11:37:28
 */
public class NumberUtils {
    private NumberUtils() {}
    public static String formatNumber(long duration) {
        DecimalFormat df = new DecimalFormat("###,##0");
        return duration == 0 ? "-" : df.format(duration);
    }
    public static String getPercentage(Number number) {
        if(number == null) {
            return "-";
        }
        DecimalFormat df = new DecimalFormat("0.00%");
        return df.format(number);
    }

    public static boolean isNumeric(String str){
        if (StringUtils.isBlank(str)) {
            return Boolean.FALSE;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String display(Long money) {
        if (money == null) {
            return "0.00";
        }
        return BigDecimal.valueOf(money).divide(BigDecimal.valueOf(100L)).setScale(2).toString();
    }

    /**
     * 金额展示或计算用
     * @return
     */
    public static BigDecimal displayForMoney(Long money) {
        if (money == null) {
            return BigDecimal.ZERO.setScale(2);
        }
        return BigDecimal.valueOf(money).divide(BigDecimal.valueOf(100L)).setScale(2);
    }
}
