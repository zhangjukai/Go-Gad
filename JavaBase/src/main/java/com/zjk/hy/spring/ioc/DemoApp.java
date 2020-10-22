package com.zjk.hy.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DemoApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig.class);
        context.refresh();
        Person person = (Person) context.getBean(Person.class);
        System.out.println(person);
        Company company = (Company) context.getBean("company");
        System.out.println(company.getPerson());
    }
}
