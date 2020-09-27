package com.zjk.hy.se.thread.blocking;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueue<Integer> PQ = new PriorityBlockingQueue();
        PQ.add(100);
        PQ.add(10);
        PQ.add(102);
        PQ.add(105);
        System.out.println(PQ.take());
        System.out.println(PQ.take());
        System.out.println(PQ.take());
        System.out.println(PQ.take());

    }
}
