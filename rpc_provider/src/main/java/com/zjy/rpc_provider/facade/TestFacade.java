package com.zjy.rpc_provider.facade;

import com.zjy.api.TestService;
import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;
import com.zjy.rpc_provider.service.TestHelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService(version = "1.0.0", interfaceClass = TestService.class)
public class TestFacade implements TestService {

    @Autowired
    private TestHelloService testHelloService;

    @Override
//    @SentinelResource(value = "TestFacade.hello")
    public HelloVo hello(HelloReq req) {
        return testHelloService.test(req);
    }

//    public static HelloVo helloBlockHandler(HelloReq req, BlockException ex) {
//        System.out.println("Oops: " + ex.getClass().getCanonicalName());
//        HelloVo vo = new HelloVo();
//        vo.setName("request limit");
//        return vo;
//    }
}
