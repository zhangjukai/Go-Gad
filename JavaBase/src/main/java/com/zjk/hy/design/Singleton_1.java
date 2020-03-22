package com.zjk.hy.design;

// 饿汉模式
public class Singleton_1 {
    // 类初始化时会立即加载该对象，线程天生安全，调用效率高
    private static Singleton_1 singleton_1 = new Singleton_1();

    private Singleton_1(){}

    public static Singleton_1 getInstance() {
        return singleton_1;
    }
}
