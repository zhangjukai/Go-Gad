package com.zjk.hy.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "CLOUD-PROVIDER-USER")
public interface UserFeignService {
    @GetMapping(value = "/user/list")
    String findList();
}
