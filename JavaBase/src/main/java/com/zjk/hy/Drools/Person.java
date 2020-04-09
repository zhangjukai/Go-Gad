package com.zjk.hy.Drools;

import java.util.List;

/**
 * 人
 */
public class Person {
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String name;
    private int age;
    private List<Car> cars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
