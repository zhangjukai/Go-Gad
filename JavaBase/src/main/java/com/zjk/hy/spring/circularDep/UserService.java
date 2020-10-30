package com.zjk.hy.spring.circularDep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    PersonService personService;

    public void print(){
        System.out.println("UserService-aaaaaaaa");
    }

    public UserService() {
        System.out.println("UserService------------------start");
    }
}
