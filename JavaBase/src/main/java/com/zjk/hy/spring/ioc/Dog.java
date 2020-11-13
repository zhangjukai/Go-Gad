package com.zjk.hy.spring.ioc;

import com.zjk.hy.spring.ioc.anno.MyTable;

@MyTable(name = "dog")
public class Dog {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
