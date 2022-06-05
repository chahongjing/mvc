package com.zjy.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjy.service.common.EnumBean;
import com.zjy.service.common.EnumUtils;
import com.zjy.service.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public String getEnums() {
        Map<String, Map<String, EnumBean>> enumBeanList = EnumUtils.getEnumBeanList();
        StringBuilder sb = new StringBuilder();
        sb.append("window.enumMap={};");
        for (Map.Entry<String, Map<String, EnumBean>> classMapEntry : enumBeanList.entrySet()) {
            sb.append(String.format("window.enumMap.%s=%s;%n", classMapEntry.getKey(), JSON.toJSONString(classMapEntry.getValue())));
        }
        return sb.toString();
    }
}
