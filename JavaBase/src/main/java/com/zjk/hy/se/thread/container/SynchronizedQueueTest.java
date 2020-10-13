package com.zjk.hy.se.thread.container;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class SynchronizedQueueTest {
    public static void main(String[] args) {
        final SynchronousQueue<String> queue = new SynchronousQueue<>();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName()+"-"+queue.take());
                    queue.put(""+(i+1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(()->{
            List<String> list = Arrays.asList("a", "b", "c", "d", "e");
            for (int i = 0; i < list.size(); i++) {
                try {
                    queue.put(list.get(i));
                    System.out.println(Thread.currentThread().getName()+"-"+queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2").start();
    }
}
