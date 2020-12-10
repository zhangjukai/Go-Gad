package com.zjk.hy.controller;

import com.zjk.hy.dto.UserDo;
import com.zjk.hy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class UserController {

    private static String USER_SERVICE = "http://CLOUD-PROVIDER-USER";
    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/user/list")
    public String findList() {
        return restTemplate.getForObject(USER_SERVICE + "/user/list", String.class);
    }


}
