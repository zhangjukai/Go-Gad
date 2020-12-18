package com.zjk.hy.dto;

import lombok.Data;

@Data
public class UserDo {
    private Integer id;
    private String name;
    private Integer age;
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
