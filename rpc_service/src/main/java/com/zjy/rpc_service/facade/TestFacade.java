package com.zjy.rpc_service.facade;

import com.zjy.api.TestService;
import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;
import com.zjy.rpc_service.service.TestHelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService(version = "1.0.0", interfaceClass = TestService.class)
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
