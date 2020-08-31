package com.zjk.hy.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {
    public static ExecutorService executorService = Executors.newScheduledThreadPool(5);
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 1; i++) {
            executorService.submit(()->{
                System.out.println(Thread.currentThread().getName()+"----执行任务中");
                countDownLatch.countDown();
            });
        }

        try {
            System.out.println("开始等待任务执行");
            countDownLatch.await();
            System.out.println("任务执行完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
