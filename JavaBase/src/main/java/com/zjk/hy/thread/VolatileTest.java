package com.zjk.hy.thread;

public class VolatileTest {
    private volatile static boolean flag = false;
    public static void main(String[] args) {
        if(flag==false){
            System.out.println(flag);
            flag = true;
        }
    }
}
