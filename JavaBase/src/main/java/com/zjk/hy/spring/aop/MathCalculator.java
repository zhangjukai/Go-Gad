package com.zjk.hy.spring.aop;

public class MathCalculator implements Calculator {
    public int div(int i,int j) {
        int i1 = i / j;
        System.out.println("计算结果："+i1);
        return i1;
    }

    @Override
    public void sayHello() {
        System.out.println("曾经的那个ta。。。。");
    }
}
