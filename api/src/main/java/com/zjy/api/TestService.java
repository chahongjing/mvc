package com.zjy.api;

import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;

public interface TestService {
    HelloVo hello(HelloReq req);
}
