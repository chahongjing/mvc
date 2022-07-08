package com.zjy.web;

import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.RedisUtils;
import com.zjy.service.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.CyclicBarrier;
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

    @Test
    void testStopWatch() {
        try {
            StopWatch stopWatch = new StopWatch("zjy");
            // 任务一模拟休眠3秒钟
            stopWatch.start("TaskOneName");
            Thread.sleep(500 * 1);
            System.out.println("当前任务名称：" + stopWatch.currentTaskName());
            stopWatch.stop();

            // 任务一模拟休眠10秒钟
            stopWatch.start("TaskTwoName");
            Thread.sleep(500 * 3);
            System.out.println("当前任务名称：" + stopWatch.currentTaskName());
            stopWatch.stop();

            // 任务一模拟休眠10秒钟
            stopWatch.start("TaskThreeName");
            Thread.sleep(500 * 2);
            System.out.println("当前任务名称：" + stopWatch.currentTaskName());
            stopWatch.stop();

            // 打印出耗时
            System.out.println("========== 1");
            System.out.println(stopWatch.prettyPrint());
            System.out.println("========== 2");
            System.out.println(stopWatch.shortSummary());
            System.out.println("========== 3");
            // stop后它的值为null
            System.out.println(stopWatch.currentTaskName());

            // 最后一个任务的相关信息
            System.out.println("========== 4");
            System.out.println(stopWatch.getLastTaskName());
            System.out.println("========== 5");
            System.out.println(stopWatch.getLastTaskInfo());
            System.out.println("========== 6");
            System.out.println(stopWatch.toString());
            System.out.println("========== 7");

            // 任务总的耗时  如果你想获取到每个任务详情（包括它的任务名、耗时等等）可使用
            System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
            System.out.println("任务总数：" + stopWatch.getTaskCount());
            System.out.println("所有任务详情：" + stopWatch.getTaskInfo());
        } catch (Exception e) {

        }
    }

    @Test
    void testApacheStopWatch() {
        try {
            //创建后立即start，常用
            org.apache.commons.lang3.time.StopWatch watch = org.apache.commons.lang3.time.StopWatch.createStarted();

            // StopWatch watch = new StopWatch();
            // watch.start();

            Thread.sleep(1000);
            System.out.println(watch.getTime());
            System.out.println("统计从开始到现在运行时间：" + watch.getTime() + "ms");

            Thread.sleep(1000);
            watch.split();
            System.out.println("从start到此刻为止的时间：" + watch.getTime());
            System.out.println("从开始到第一个切入点运行时间：" + watch.getSplitTime());


            Thread.sleep(1000);
            System.out.println("从开始到第二个切入点运行前时间：" + watch.getSplitTime());
            watch.split();
            System.out.println("从开始到第二个切入点运行时间：" + watch.getSplitTime());

            // 复位后, 重新计时
            watch.reset();
            watch.start();
            Thread.sleep(1000);
            System.out.println("重新开始后到当前运行时间是：" + watch.getTime());

            // 暂停 与 恢复
            watch.suspend();
            System.out.println("暂停2秒钟");
            Thread.sleep(2000);

            // 上面suspend，这里要想重新统计，需要恢复一下
            watch.resume();
            System.out.println("恢复后执行的时间是：" + watch.getTime());

            Thread.sleep(1000);
            watch.stop();

            System.out.println("花费的时间》》" + watch.getTime() + "ms");
            // 直接转成s
            System.out.println("花费的时间》》" + watch.getTime(TimeUnit.SECONDS) + "s");
        } catch (Exception e) {

        }
    }

    @Test
    void testRedisSet() {
        redisUtils.test("1");
    }
}
