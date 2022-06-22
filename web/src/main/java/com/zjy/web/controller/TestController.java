package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.service.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    @Value("${mykey1}")
    private String myKey1;
    @Value("${mykey2.ab}")
    private String myKey2;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/index")
    public BaseResult<Map<String, Object>> index() {
        Map<String, Object> map = new HashMap<>();
        map.put("myKey1", myKey1);
        map.put("myKey2", myKey2);
        return BaseResult.ok(map);
    }

    @GetMapping("/testTransaction")
    public BaseResult<Map<String, Object>> testTransaction() {
        userInfoService.testTransaction();
        return BaseResult.ok();
    }
}
