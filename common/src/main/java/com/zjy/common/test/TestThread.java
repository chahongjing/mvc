package com.zjy.common.test;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TestThread {
    private AtomicLong counter = new AtomicLong();
    private Semaphore semaphore = new Semaphore(20);
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    private CountDownLatch cdl;
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            20,
            5L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 信号量
     */
    public void mulitThreadUploadDirToFdsV2() {
        Queue<String> filePathResult = new LinkedList<>();
        filePathResult.offer("for test");
        String filePath;
        try {
            cdl = new CountDownLatch(filePathResult.size());
            // 多线程处理
            while ((filePath = filePathResult.poll()) != null) {
                semaphore.acquire();
                File f = new File(filePath);
                executorService.execute(() -> {
                    uploadFileToFds(f);
                });
            }
            cdl.await();
            log.info("处理完成");
        } catch (InterruptedException e) {
            log.error("处理失败, count:{}", counter.get(), e);
        }
    }

    public void mulitThreadUploadDirToFdsV3() {
        Queue<String> filePathResult = new LinkedList<>();
        filePathResult.offer("for test");
        String filePath;
        try {
            cdl = new CountDownLatch(filePathResult.size());
            // 多线程处理
            while ((filePath = filePathResult.poll()) != null) {
                File f = new File(filePath);
                threadPoolExecutor.execute(() -> {
                    uploadFileToFds(f);
                });
            }
            cdl.await();
            log.info("处理完成");
        } catch (InterruptedException e) {
            log.error("处理失败, count:{}", counter.get(), e);
        }
    }

    public void uploadFileToFds(File file) {
        try {
            // todo: 处理业务
            // xxxxxxxxxxxxx
            long count = counter.incrementAndGet();
            if (count % 500 == 0) {
                log.info("上传文件量：{}", count);
            }
        } catch (Exception ex) {
            log.error("处理报错了：{}", file.getPath(), ex);
        } finally {
            if (cdl != null) {
                cdl.countDown();
            }
            semaphore.release();
        }
    }


    public static class CyclicBarrierTest {
        private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        public static class Surmount implements Runnable {
            @Override
            public void run() {
                try {
                    for (int i = 1; i < 4; i++) {
                        Random rand = new Random();
                        int randomNum = rand.nextInt((3000 - 1000) + 1) + 1000;//产生1000到3000之间的随机整数
                        Thread.sleep(randomNum);
                        String name = Thread.currentThread().getName();
                        System.out.println(name + "翻过了第" + i + "个障碍");
                        cyclicBarrier.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            for (int i = 1; i < 6; i++) {
                Thread thread = new Thread(new Surmount(), "选手" + i);
                thread.start();
            }
            System.out.println("main is end");
        }
    }
}
