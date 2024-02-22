package com.zjk.hy.se.thread.volatileDemo;

class SharedObject {
    volatile int counter = 0;

    public void increment() {
        counter++;  // 这个操作不是原子性的。
    }
}

public class VolatileDemo {
    // sharedObject 引用是可见的，但是 sharedObject 对象的状态可能不是。
    private volatile SharedObject sharedObject = new SharedObject();

    public static void main(String[] args) {

        VolatileDemo demo = new VolatileDemo();
        // Thread 1: Increment the counter 1000 times
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                demo.sharedObject.increment();
            }
        });

        // Thread 2: Increment the counter 1000 times
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                demo.sharedObject.increment();
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        try {
            // Wait for the threads to complete
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // re-interrupt the thread
            e.printStackTrace();
        }

        // Print the final value of the counter
        System.out.println(demo.sharedObject.counter);
    }
}
