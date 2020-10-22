package com.zjk.hy.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;

public class Company {
    private String name;
    @Autowired
    private Person person;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
