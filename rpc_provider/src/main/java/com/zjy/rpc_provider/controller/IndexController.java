package com.zjy.rpc_provider.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
    @SentinelResource(value = "index_csp", blockHandler = "indexBlockHandler")
    public String index() throws BlockException {
        HelloReq req = new HelloReq();
        req.setName("zjy from service controller");
        return testService.hello(req).getName();
    }

    public static String indexBlockHandler(BlockException ex) {
        System.out.println("Oops: " + ex.getClass().getCanonicalName());
        return "request limit";
    }
}
