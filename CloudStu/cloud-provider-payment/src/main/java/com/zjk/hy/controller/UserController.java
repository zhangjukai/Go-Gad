package com.zjk.hy.controller;

import com.zjk.hy.service.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/*@DefaultProperties(defaultFallback = "defaultFallback")*/
@RestController
public class UserController {

    private static String USER_SERVICE = "http://CLOUD-PROVIDER-USER";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserFeignService userFeignService;

    @GetMapping(value = "/user/list")
    public String findList() {
        return restTemplate.getForObject(USER_SERVICE + "/user/list", String.class);
    }
    /*@HystrixCommand*/
    @GetMapping(value = "/feign/user/list")
    public String findListByFeign() {
        return userFeignService.findList();
    }

    @GetMapping(value = "/user/{id}")
    public String userInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(USER_SERVICE + "/user/"+id, String.class);
    }


    public String defaultFallback(){
        return "服务调用异常。。。。。。o(╥﹏╥)o。。。。";
    }

}
