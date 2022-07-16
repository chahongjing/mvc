package com.zjy.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @date 2019-09-29 16:11:58
 */
public class PhoneUtils {
    public static String markPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        phone = phone.trim();
        if (phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    /**
     * 校验手机号码格式，座机号不通过校验
     *
     * @param mobiles
     * @return
     * @author
     */
    public static boolean isMobile(String mobiles) {
        if (StringUtils.isBlank(mobiles)) {
            return Boolean.FALSE;
        }
        Matcher m = RegExpUtils.phoneReg1.matcher(mobiles);
        return m.matches();
    }
}
