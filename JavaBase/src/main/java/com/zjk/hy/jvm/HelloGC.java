package com.zjk.hy.jvm;

import java.util.List;
import java.util.LinkedList;

public class HelloGC {
    public static void main(String[] args) {
        System.out.println("HelloGC!");
        List list = new LinkedList();
        for(;;) {
            byte[] b = new byte[1024*1024];
            list.add(b);
        }
    }
}