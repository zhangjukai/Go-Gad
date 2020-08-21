package com.zjk.hy.thread.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VolatileTest {
    private volatile static boolean flag = false;
    public static void main(String[] args) {
        if(flag==false){
            System.out.println(flag);
            flag = true;
        }


        ReentrantLock lock  = new ReentrantLock();
        Condition condition = lock.newCondition();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
