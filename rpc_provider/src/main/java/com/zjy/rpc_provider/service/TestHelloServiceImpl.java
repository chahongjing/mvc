package com.zjy.rpc_provider.service;

import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestHelloServiceImpl implements TestHelloService {
    @Override
    public HelloVo test(HelloReq req) {
        HelloVo vo = new HelloVo();
        vo.setName(req.getName() + " - " + Math.random());
        log.info("test method, name: {}", vo.getName());
        return vo;
    }
}
