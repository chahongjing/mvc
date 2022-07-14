package com.zjy.rpc_consumer.controller;

import com.zjy.api.vo.HelloVo;
import com.zjy.rpc_consumer.rpc.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {
    @Autowired
    private RpcService rpcService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public HelloVo index() {
        return rpcService.test();
    }
}
