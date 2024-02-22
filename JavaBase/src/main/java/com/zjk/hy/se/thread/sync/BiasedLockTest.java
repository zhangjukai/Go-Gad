package com.zjk.hy.se.thread.sync;

import org.openjdk.jol.info.ClassLayout;

public class BiasedLockTest {
    private static Object obj = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (obj) {
                System.out.println(ClassLayout.parseInstance(obj).toPrintable());
            }
        }).start();
        /*new Thread(() -> {
            synchronized (obj) {
                System.out.println(ClassLayout.parseInstance(obj).toPrintable());
            }
        }).start();*/
    }
}
