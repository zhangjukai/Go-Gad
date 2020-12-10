package com.zjk.hy.dto;

import lombok.Data;

@Data
public class UserDo {
    private int id;
    private String name;
    private int age;
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
