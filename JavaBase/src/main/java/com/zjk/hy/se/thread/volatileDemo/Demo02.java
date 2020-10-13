package com.zjk.hy.se.thread.volatileDemo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Demo02 {
    private static volatile ArrayList<Integer> list = new ArrayList<>();
    static Thread t1=null,t2=null;
    public static void main(String[] args) {
        t1 = new Thread(()->{
            System.out.println("t1 開始執行");
            for (int i = 0; i < 10; i++) {
                list.add(i);
                System.out.println("i:"+i);
                if(i==5){
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        });

        t2 = new Thread(()->{
            System.out.println("t2开始执行");
            LockSupport.park();
            System.out.println("到达t2退出点");
            LockSupport.unpark(t1);
        });
        t1.start();
        t2.start();
    }
}
