package com.zjk.hy.spring.aop;

public class DefaultMathCalculator implements Calculator {
    public int div(int i,int j) {
        int i1 = (i*10) / j;
        System.out.println("计算结果："+i1);
        return i1;
    }

    @Override
    public void sayHello() {
        System.out.println("思念-远方的你");
    }
}
