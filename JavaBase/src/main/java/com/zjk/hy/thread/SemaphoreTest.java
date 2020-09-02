package com.zjk.hy.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newScheduledThreadPool(5);
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"：正在执行。。。。。。。。。。。。。。");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName()+"：执行完成。。。。。。。。。。。。。。");
                    System.out.println("=================================");
                    System.out.println("=================================");
                    System.out.println("=================================");
                    semaphore.release();
                }

            });
        }
        executorService.shutdown();
    }
}
