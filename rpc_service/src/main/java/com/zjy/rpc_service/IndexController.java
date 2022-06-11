package com.zjy.rpc_service;

import com.zjy.api.TestService;
import com.zjy.api.request.HelloReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {
    @Autowired
    private TestService testService;
    /**
     * 首页
     * @return
     */
    @GetMapping("/")
    public String index() {
        HelloReq req = new HelloReq();
        req.setName(String.valueOf(Math.random()));
        return testService.hello(req).getName();
    }
}
