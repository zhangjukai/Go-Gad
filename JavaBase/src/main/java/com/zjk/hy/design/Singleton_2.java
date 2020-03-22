package com.zjk.hy.design;

// 懒汉式
public class Singleton_2 {
    // 类初始化时不会初始化该对象，真正需要使用时才会创建该对象
    private static Singleton_2 singleton2;
    private Singleton_2 () {
    }
    public synchronized static Singleton_2 getInstance(){
        if (singleton2 == null) {
            singleton2 = new Singleton_2();
        }
        return singleton2;
    }
}
