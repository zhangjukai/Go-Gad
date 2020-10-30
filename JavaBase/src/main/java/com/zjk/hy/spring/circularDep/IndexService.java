package com.zjk.hy.spring.circularDep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexService {

    @Autowired
    UserService userService;

    public void print(){
        System.out.println("IndexService-aaaaaaaaaaaa");
    }

    public IndexService() {
        System.out.println("IndexService-------construct");
    }
}
