package com.zjy.web;

import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.RedisUtils;
import com.zjy.service.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class WebApplicationTests {

    @Autowired
    private TestService testService;
    @Autowired
    private RedisUtils redisUtils;

    private CyclicBarrier cb = new CyclicBarrier(5);

    @Test
    void contextLoads() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("z");

//        RedisScript<Long> longRedisScript = redisUtils.incrLimitExpScript();
//        System.out.println(longRedisScript.getScriptAsString());
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 5; i++) {
//            executorService.execute(() -> {
//                try {
//                    cb.await();
//                    System.out.println("输出结果：" + testService.test(userInfo));
//                } catch (InterruptedException | BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println(redisUtils.lock1("a", "b", 20));
        System.out.println(redisUtils.lock("a", "b", 20, TimeUnit.SECONDS));
        System.out.println(redisUtils.lock1("a", "b", 20));
        System.out.println(redisUtils.unlock1("a", "c"));
        System.out.println(redisUtils.unlock1("a", "b"));
        System.out.println(redisUtils.unlock1("a", "b"));
    }
}
