package com.zjk.hy.design;

import java.io.ObjectStreamException;
import java.io.Serializable;

// 内部静态类的方式
public class Singleton3 implements Serializable {
    private Singleton3 () {
        if(null!=SingletonClassInstance.singleton3){
            throw new RuntimeException("该类只允许有一个实例");
        }
    }
    private static  class SingletonClassInstance {
        private static final Singleton3 singleton3 = new Singleton3();
    }

    // 没有同步，调用效率高
    public static Singleton3 getInstance(){
        return SingletonClassInstance.singleton3;
    }

    // 防止反序列化获取多个对象的漏洞
    private Object readResolve() throws ObjectStreamException {
        return SingletonClassInstance.singleton3;
    }
}
