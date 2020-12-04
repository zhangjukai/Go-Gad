package com.zjk.hy.spring.circularDep;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserService  {

    private IndexService indexService;
    public void print(){
        System.out.println("UserService-aaaaaaaa"+indexService);
    }

    public UserService(IndexService indexService) {
        this.indexService = indexService;
    }
    public UserService(){

    }




    /*@Lookup(value = "indexService")
    protected IndexService createIndexService(){
        return null;
    }*/

}
