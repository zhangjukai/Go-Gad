package com.zjk.hy.design.singleton;
// 使用枚举实现单例模式 优点:实现简单、枚举本身就是单例，由jvm从根本上提供保障!避免通过反射和反序列化的漏洞 缺点没有延迟加载
public class Singleton4 {
    private Singleton4(){}
    private static enum SingletonEnum {
        INSTANCE;
        //枚举元素为单列
        private Singleton4 singleton4;

        private SingletonEnum(){
            singleton4 = new Singleton4();
        }

        public Singleton4 getInstance(){
            return singleton4;
        }
    }

    public static Singleton4 getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }
}
