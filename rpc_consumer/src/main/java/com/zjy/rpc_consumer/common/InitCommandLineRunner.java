package com.zjy.rpc_consumer.common;

import com.alibaba.csp.sentinel.init.InitExecutor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        InitExecutor.doInit();
    }
}
