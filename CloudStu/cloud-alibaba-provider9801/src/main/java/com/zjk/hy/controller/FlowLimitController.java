package com.zjk.hy.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class FlowLimitController {
    @GetMapping("/testA")
    @SentinelResource(value = "testA",blockHandler = "testAblockHandler")
    public String testA(){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA");
        return "--------------restA";
    }

    @GetMapping("/testB")
    public String testB(){
        return "--------------restB";
    }

    public String testAblockHandler(BlockException exception){
        return "异常了。。。。。";
    }
}
