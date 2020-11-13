package com.zjk.hy.spring.ioc;

import com.zjk.hy.spring.ioc.anno.MyTable;

public class Demo04 {
    public static void main(String[] args) {
        Class<? extends Dog> dogClass = Dog.class;
        if (dogClass.isAnnotationPresent(MyTable.class)){
            MyTable myTable = dogClass.getAnnotation(MyTable.class);
            System.out.println( myTable.name());
        }
    }
}
