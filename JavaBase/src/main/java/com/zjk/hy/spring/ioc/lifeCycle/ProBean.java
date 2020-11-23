package com.zjk.hy.spring.ioc.lifeCycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProBean {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
