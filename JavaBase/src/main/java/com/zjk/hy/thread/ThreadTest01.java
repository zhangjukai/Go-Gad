package com.zjk.hy.thread;



class ThreadDemo01 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("i:"+i);
        }
    }
}

/**
 * @author zjk
 * @date 2019/4/17 - 20:58
 */
public class ThreadTest01 {
    public static void main(String[] args) {
        System.out.println("主线程开始！");
        ThreadDemo01 demo01 = new ThreadDemo01();
        demo01.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("main.i:"+i);
        }
        System.out.println("主线程结束");
    }
}

