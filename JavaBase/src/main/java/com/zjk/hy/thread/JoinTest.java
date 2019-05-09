package com.zjk.hy.thread;

/**
 * @author zjk
 * @date 2019/5/9 - 21:55
 */
public class JoinTest {
    public static void main(String[] args) {
        JoinThread joinThread = new JoinThread();
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0;i<100;i++) {
            System.out.println("main.i="+i);
        }
    }
}

class JoinThread extends  Thread {
    @Override
    public void run() {
        for (int i=0;i<100;i++) {
            System.out.println("i="+i);
        }
    }
}