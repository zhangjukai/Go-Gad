package com.zjk.hy.spring.circularDep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonService {
    @Autowired
    IndexService IndexService;
}
