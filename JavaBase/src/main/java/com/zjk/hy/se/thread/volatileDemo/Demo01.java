package com.zjk.hy.se.thread.volatileDemo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Demo01 {
    private static volatile ArrayList<Integer> list = new ArrayList<>();
    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                list.add(i);
                System.out.println("i:"+i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            while (true) {
                if (list.size()==5) {
                    System.out.println("到达退出点");
                    break;
                }
            }
        }).start();
    }
}
