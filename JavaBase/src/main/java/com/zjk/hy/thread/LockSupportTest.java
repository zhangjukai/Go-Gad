package com.zjk.hy.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupportTest {
    public static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable runnable = ()->{
            try {
                System.out.println("第一个线程执行。。。。");
                LockSupport.park();
                System.out.println("第一个线程被唤醒了。。。。");
            } finally {
            }
        };
        Thread thread = new Thread(runnable);
        executorService.submit(thread);
        LockSupport.unpark(thread);
        executorService.shutdown();
    }
}
