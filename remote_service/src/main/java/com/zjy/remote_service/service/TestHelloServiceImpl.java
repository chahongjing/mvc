package com.zjy.remote_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestHelloServiceImpl implements TestHelloService {
    @Override
    public void test() {
        log.info("test method");
    }
}
