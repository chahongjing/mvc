package com.zjy.rpc_provider.service;

import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;

public interface TestHelloService {
    HelloVo test(HelloReq req);
}
