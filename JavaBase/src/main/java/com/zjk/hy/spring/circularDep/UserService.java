package com.zjk.hy.spring.circularDep;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UserService  {

    public void print(){
        System.out.println("UserService-aaaaaaaa");
    }

    public UserService() {
        System.out.println("UserService------------------start");
    }

    /*@Lookup(value = "indexService")
    protected IndexService createIndexService(){
        return null;
    }*/

}
