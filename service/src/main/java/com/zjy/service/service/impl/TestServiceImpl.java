package com.zjy.service.service.impl;

import com.zjy.baseframework.annotations.LimitByCount;
import com.zjy.baseframework.annotations.LogMethod;
import com.zjy.common.utils.JsonUtils;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private JsonUtils jsonUtils;

    @LogMethod
    @Override
    @LimitByCount(count = 4, timeout = 10)
    public String test(UserInfo userInfo) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonUtils.toJSON(userInfo);
    }
}
