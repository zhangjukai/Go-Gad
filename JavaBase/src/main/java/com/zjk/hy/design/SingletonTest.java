package com.zjk.hy.design;

public class SingletonTest {
    public static void main(String[] args) {
        /*Singleton_1 Singleton_1_1 = Singleton_1.getInstance();
        Singleton_1 Singleton_1_2 = Singleton_1.getInstance();
        System.out.println(Singleton_1_1==Singleton_1_2);*/
        Singleton_2 singleton2_1 = Singleton_2.getInstance();
        Singleton_2 singleton2_2 = Singleton_2.getInstance();
        System.out.println(singleton2_1 == singleton2_1);
        Singleton3 singleton3_1 = Singleton3.getInstance();
        Singleton3 singleton3_2 = Singleton3.getInstance();
        System.out.println(singleton3_1==singleton3_2);
        Singleton4 singleton4_1 = Singleton4.getInstance();
        Singleton4 singleton4_2 = Singleton4.getInstance();
        System.out.println(singleton4_1 == singleton4_2);
    }
}
