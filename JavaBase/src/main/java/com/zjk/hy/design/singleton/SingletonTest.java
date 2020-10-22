package com.zjk.hy.design.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SingletonTest {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        /*Singleton_enum instance = Singleton_enum.INSTANCE;
        instance.printn();
        Constructor<Singleton_enum> constructor = Singleton_enum.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Singleton_enum instance1 = constructor.newInstance("INSTANCE", 0);*/


        Class<Singleton_2> clazz = Singleton_2.class;
        Constructor<Singleton_2> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Singleton_2 singleton_2 = constructor.newInstance();
        Singleton_2 instance = Singleton_2.getInstance();
        System.out.println(instance==singleton_2);

    }
}
