package com.zjy.service.service.impl;

import com.zjy.baseframework.interfaces.ICache;
import com.zjy.common.common.EnumBean;
import com.zjy.common.utils.EnumUtils;
import com.zjy.common.utils.JsonUtils;
import com.zjy.service.enums.SwitchEnum;
import com.zjy.service.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private ICache cache;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JsonUtils jsonUtils;

    @Override
    public String getEnums() {
        Map<String, Map<String, EnumBean>> enumBeanList = EnumUtils.getEnumBeanList();
        StringBuilder sb = new StringBuilder();
        sb.append("window.enumMap={};");
        for (Map.Entry<String, Map<String, EnumBean>> classMapEntry : enumBeanList.entrySet()) {
            sb.append(String.format("window.enumMap.%s=%s;%n", classMapEntry.getKey(), jsonUtils.toJSON(classMapEntry.getValue())));
        }
        return sb.toString();
    }

    @Override
    public boolean isSwitchOpen(SwitchEnum se) {
        return SwitchEnum.SWITCH_OPEN.equals(cache.hGet(SwitchEnum.SWITCH_KEY, se.toString()));
    }

    public String getMsg(String code) {
        Object[] param = new Object[1];
        param[0] = new Date();
        return messageSource.getMessage(code, param, Locale.getDefault());
    }
}
