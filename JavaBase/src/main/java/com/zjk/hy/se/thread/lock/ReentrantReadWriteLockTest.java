package com.zjk.hy.se.thread.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    private final static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    private final static Lock writeLock = reentrantReadWriteLock.writeLock();
    private final static Lock readLock = reentrantReadWriteLock.readLock();
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.submit(()->read());
        System.out.println(reentrantReadWriteLock.getQueueLength());
        executorService.submit(()->write());
        System.out.println(reentrantReadWriteLock.getQueueLength());
        executorService.submit(()->read());
        executorService.submit(()->read());
        executorService.submit(()->read());
        executorService.submit(()->read());
        executorService.submit(()->read());
        System.out.println(reentrantReadWriteLock.getQueueLength());
        executorService.shutdown();
    }

    public static void write(){
       try {
           System.out.println(Thread.currentThread().getName()+"write start");
           writeLock.lock();
           System.out.println(Thread.currentThread().getName()+":写入数据");
           try {
               TimeUnit.SECONDS.sleep(2);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }finally {
            writeLock.unlock();
       }
    }

    public static void read() {
        try {
            System.out.println(Thread.currentThread().getName()+"read start");
            readLock.lock();
            System.out.println(Thread.currentThread().getName()+":读取数据");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }finally {
            readLock.unlock();
        }
    }
}
