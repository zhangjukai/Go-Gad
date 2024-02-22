package com.zjk.hy.se;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class ObjMemoryDemo {
    /*public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        System.out.println(obj.hashCode());
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
        new Thread(()->{
            synchronized (obj) {
                System.out.println(ClassLayout.parseInstance(obj).toPrintable());
            }
        }).start();
        new Thread(()->{
            synchronized (obj) {
                System.out.println(ClassLayout.parseInstance(obj).toPrintable());
            }
        }).start();
    }*/
    public static void main(String[] args) {
        User obj = new User();
        obj.setName("张局开");
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
