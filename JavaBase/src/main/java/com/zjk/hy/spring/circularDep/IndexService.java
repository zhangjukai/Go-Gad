package com.zjk.hy.spring.circularDep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class IndexService {
/*
    @Autowired
    UserService userService;*/


    public void print(){
        System.out.println("IndexService-aaaaaaaaaaaa");
    }

    public IndexService() {
        System.out.println("IndexService-------construct");
    }
}
