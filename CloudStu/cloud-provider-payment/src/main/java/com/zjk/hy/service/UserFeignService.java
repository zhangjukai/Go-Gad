package com.zjk.hy.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zjk.hy.service.impl.UserFeignFallbackFactoryService;
import com.zjk.hy.service.impl.UserFeignFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


//@FeignClient(value = "CLOUD-PROVIDER-USER",fallback = UserFeignFallbackService.class)
@FeignClient(value = "CLOUD-PROVIDER-USER",fallbackFactory = UserFeignFallbackFactoryService.class)
public interface UserFeignService {
    @GetMapping(value = "/user/list")
    String findList();


}
