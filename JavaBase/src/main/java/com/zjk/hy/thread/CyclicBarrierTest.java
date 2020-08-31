package com.zjk.hy.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
    public static ExecutorService executorService = Executors.newScheduledThreadPool(5);
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()-> System.out.println(Thread.currentThread().getName()
                +"Fuck 就这???"));
        for (int i = 0; i < 5; i++) {
            executorService.submit(()->{
                System.out.println(Thread.currentThread().getName()+"----bbbbbbbbbbbbbbbbb");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"----执行任务中");
            });
        }
        System.out.println("aaaaaaaaaaaaaaa");
        executorService.shutdown();
    }
}
