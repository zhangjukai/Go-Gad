package com.zjk.hy.se.thread.sync;

public class CacheLineTest2 {
    private static class Padding {
        //7*8=56字节
        public volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    private static class T extends Padding {
        //8字节
        public volatile long x = 0L;
    }

    public static T arr[] = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

            for (long i = 0; i < 1000_0000L; i++) {
                //一个缓存行刚好64字节，则不会再有Main2中的消耗
                arr[0].x = i;
            }
        });

        Thread t2 = new Thread(() -> {

            for (long i = 0; i < 1000_0000L; i++) {
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
