package com.zjy.rpc_consumer.rpc;

import com.zjy.api.TestService;
import com.zjy.api.request.HelloReq;
import com.zjy.api.vo.HelloVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RpcServiceImpl implements RpcService {
    //    @DubboReference(version = "1.0.0", url="127.0.0.1:20880")
    @DubboReference(version = "1.0.0")
    private TestService testService;

    @Override
    public HelloVo test() {
        HelloReq req = new HelloReq();
        req.setName("zjy from rpc consumer");
        return testService.hello(req);
    }
}
