package com.zjy.service.service.impl;

import com.zjy.baseframework.annotations.LogMethod;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.component.JacksonUtil;
import com.zjy.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @LogMethod
    @Override
    public String test(UserInfo userInfo) {
        return JacksonUtil.toJSON(userInfo);
    }
}
