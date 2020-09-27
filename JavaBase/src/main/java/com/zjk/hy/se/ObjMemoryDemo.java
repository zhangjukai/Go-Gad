package com.zjk.hy.se;

import org.openjdk.jol.info.ClassLayout;

public class ObjMemoryDemo {
    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(obj.hashCode());
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        /*User user = new User();
        System.out.println(ClassLayout.parseInstance(user).toPrintable());*/
    }
}
