package com.zjk.hy.service.impl;

import com.zjk.hy.service.UserFeignService;
import org.springframework.stereotype.Service;

@Service
public class UserFeignFallbackService implements UserFeignService {
    @Override
    public String findList() {
        return "feign调用，服务降级。。。。。。。o(╥﹏╥)o";
    }
}
