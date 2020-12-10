package com.zjk.hy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("discovery")
public class DiscoveryController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("get")
    public Object discovery() {
        discoveryClient.getServices().forEach(service -> {
            System.out.println(service);
        });
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PROVIDER-PAYMENT");
        instances.forEach(instance -> {
            System.out.println(instance);
        });
        return discoveryClient;
    }
}
