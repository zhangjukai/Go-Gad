package com.zjk.hy.thread;

/**
 * @author zjk
 * @date 2019/5/9 - 20:59
 */
public class DaemonTest {
    public static void main(String[] args) {
        DaemonThread daemonThread = new DaemonThread();
        //daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(5000);
            System.out.println("执行完喽！！！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class DaemonThread extends Thread {
    private int i = 0;
    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i="+i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
