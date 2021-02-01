package com.zjk.hy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ConfigController {
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("info")
    public String findInfo(){
        return configInfo;
    }
}
