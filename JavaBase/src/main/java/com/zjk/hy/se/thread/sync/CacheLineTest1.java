package com.zjk.hy.se.thread.sync;

public class CacheLineTest1 {
    public static class T {
        //8字节，尚未达到缓存行最大字节，故arr[0]与arr[1]可能在同一个缓存行
        private volatile long x = 0L;
    }

    private static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

            for (long i = 0; i < 1000_0000L; i++) {
                //volatile的 缓存一致性协议MESI 或者锁总线 保证线程t2的可能性，会消耗时间
                arr[0].x = i;
            }
        });

        Thread t2 = new Thread(() -> {

            for (long i = 0; i < 1000_0000L; i++) {
                //同t1
                arr[1].x = i;
            }
        });
        long start = System.nanoTime();

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_0000);
    }

}
