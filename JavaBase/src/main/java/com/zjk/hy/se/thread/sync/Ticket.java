package com.zjk.hy.se.thread.sync;

/**
 * @author zjk
 * @date 2019/5/28 - 21:48
 */
public class Ticket implements Runnable {
    static String obj = "key";
    private int num = 0; // 出票数
    private int count = 10; // 剩余票数

    boolean flag = false;

    @Override
    public void run() {
        while (true) {
            // 没有余票时，跳出循环
            synchronized (obj){
                if (count <= 0) {
                    break;
                }
                num++;
                count--;
                try {
                    Thread.sleep(500);// 模拟网络延时
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("显示出票信息：" + Thread.currentThread().getName()
                        + "抢到第" + num + "张票，剩余" + count + "张票");
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Ticket ticket=new Ticket();
        // 　实例化几个抢票用户
        Thread mary = new Thread(ticket, "玛丽");
        Thread jack = new Thread(ticket, "杰克");
        mary.start();
        jack.start();
    }
}