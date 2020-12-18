package com.zjk.hy.controller;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zjk.hy.dto.UserDo;
import com.zjk.hy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    UserService userService;

    @GetMapping(value = "/user/list")
    public String findList() {
        List<UserDo> list = userService.findList();
        if(CollectionUtils.isEmpty(list)) {
            return "没有获取到数据，o(╥﹏╥)o"+serverPort;
        }
        return list.toString()+"——"+serverPort;
    }

    @HystrixCommand(fallbackMethod = "payCircuitBreakerFallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器 默认为false
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60") // 失败率达到多少后跳闸 百分比
    })
    @GetMapping("/user/{id}")
    public String payCircuitBreaker(@PathVariable("id") Integer id) {
        if(id<0){
            throw new RuntimeException("id不能小于0 ");
        }
        return Thread.currentThread().getName()+"调用成功，流水号为："+IdUtil.simpleUUID();
    }

    private String payCircuitBreakerFallback(Integer id) {
        return "id不能为负数，请稍后再试，o(╥﹏╥)o，ID："+id;
    }
}

