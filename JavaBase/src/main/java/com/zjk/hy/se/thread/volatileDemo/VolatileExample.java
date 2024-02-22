package com.zjk.hy.se.thread.volatileDemo;

public class VolatileExample {
    private volatile int number = 0;

    public int getNumber() {
        return this.number;
    }

    public void incrementNumber() {
        this.number++;
    }

    public static void main(String[] args) {
        System.out.println("aaa");
    }
}
