package com.zjy.remote_service.facade;

import com.zjy.api.TestService;
import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;
import com.zjy.remote_service.service.TestHelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestFacade implements TestService {

    @Autowired
    private TestHelloService testHelloService;

    @Override
    public HelloVo hello(HelloReq req) {
        HelloVo vo = new HelloVo();
        vo.setName(req.getName() + Math.random());
        testHelloService.test();
        return vo;
    }
}
