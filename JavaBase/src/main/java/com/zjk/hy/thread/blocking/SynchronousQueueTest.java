package com.zjk.hy.thread.blocking;

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueTest {

    public static void main(String[] args) {
        SynchronousQueue synchronousQueue = new SynchronousQueue<String>();
        boolean b = synchronousQueue.offer("aa");
        System.out.println(b);

    }

}
