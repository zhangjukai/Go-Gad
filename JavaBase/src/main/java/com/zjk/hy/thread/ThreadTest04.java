package com.zjk.hy.thread;

/**
 * @author zjk
 * @date 2019/5/6 - 21:03
 */
public class ThreadTest04 {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        try {
            Thread.sleep(2000);
            myThread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("aaaaaaaaaa");
            e.printStackTrace();
        }

    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 1000000000; i++) {
                if (this.isInterrupted()) {
                    System.out.println("已经是停止状态！");
                    throw new InterruptedException();
                }
                System.out.println("i=" + (i + 1));
            }
            System.out.println(" for back !!!!!");
        } catch (InterruptedException e) {
            System.out.println(" in exception !!!");
            e.printStackTrace();
        }

    }
}