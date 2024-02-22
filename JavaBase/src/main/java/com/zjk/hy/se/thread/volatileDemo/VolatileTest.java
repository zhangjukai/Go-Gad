package com.zjk.hy.se.thread.volatileDemo;

public class VolatileTest {
    static volatile boolean flag = false;
    public static void main(String[] args) {
        if(flag==false){
            System.out.println(flag);
            flag = true;
        }
    }
}
