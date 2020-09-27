package com.zjk.hy.se.thread.sync;

public class SynchronizedTest {
    private static Object obj = new Object();
    public static void main(String[] args) {
        synchronized (obj) {
            System.out.println("aaaaa");
        }
    }
    public synchronized void test(){
        System.out.println("bbbbbb");
    }
}
