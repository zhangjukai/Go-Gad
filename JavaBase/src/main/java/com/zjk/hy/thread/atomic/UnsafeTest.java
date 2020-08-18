package com.zjk.hy.thread.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

public class UnsafeTest {
    public static void main(String[] args) {
        // Unsafe unsafe = Unsafe.getUnsafe();
        /*try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (sun.misc.Unsafe) field.get(null);
            System.out.println(unsafe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }*/

        System.out.println(new UnsafeTest().get());

    }

    public int get(){
        for(int i=0;i<10;i++) {
            System.out.println("aaaaaaaaaaaaa:"+i);
            if(i==5){
                LockSupport.park();
                return i;
            }
            System.out.println("bbbbbbbbbbbbbbb:"+i);
        }
        return 0;
    }


}
