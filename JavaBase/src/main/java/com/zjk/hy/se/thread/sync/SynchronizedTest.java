package com.zjk.hy.se.thread.sync;

import java.util.Collections;

public class SynchronizedTest {
    private static Object obj = new Object();
    public static void main(String[] args) {
        synchronized (obj) {
            System.out.println("aaaaa");
        }
    }
}
