package com.zjk.hy.thread.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zjk
 * @date 2019/10/15 - 20:24
 */
public class ThreadPoolTest01 {
    public static void main(String[] args) {
        /*ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            service.execute(() -> System.out.println(Thread.currentThread().getName()+","+temp));
        }*/

        /*ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            service.execute(() -> System.out.println(Thread.currentThread().getName()+","+temp));
        }*/

        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            service.schedule(() -> System.out.println(Thread.currentThread().getName()+","+temp),3, TimeUnit.SECONDS);
        }
        service.shutdown();
    }
}
