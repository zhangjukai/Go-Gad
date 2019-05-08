package com.zjk.hy.thread;

/**
 * @author zjk
 * @date 2019/4/17 - 21:08
 */

class ThreadDemo02 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.println("ii:"+i);
        }
    }
}

public class ThreadTest02 {
    public static void main(String[] args) {
        System.out.println("主线程开始！");
        ThreadDemo02 demo02 = new ThreadDemo02();
        Thread thread = new Thread(demo02);
        thread.start();
        for (int i = 0; i < 10000; i++) {
            System.out.println("main.i:"+i);
        }
        System.out.println("主线程结束");
    }
}
