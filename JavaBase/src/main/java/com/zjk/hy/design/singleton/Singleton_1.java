package com.zjk.hy.design.singleton;

import java.io.Serializable;

// 饿汉模式
public class Singleton_1 implements Serializable {
    // 类初始化时会立即加载该对象，线程天生安全，调用效率高
    private static Singleton_1 singleton_1 = new Singleton_1();

    private Singleton_1(){
        if(singleton_1!=null){
            throw new RuntimeException("只能创建一个对象");
        }
    }

    public static Singleton_1 getInstance() {
        return singleton_1;
    }
}
