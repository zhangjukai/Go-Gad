package com.zjk.hy.service.impl;

import com.zjk.hy.service.UserFeignService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallbackFactoryService implements FallbackFactory<UserFeignService> {
    @Override
    public UserFeignService create(Throwable throwable) {
        return new UserFeignService() {
            @Override
            public String findList() {
                return "FallbackFactory-feign调用，服务降级。。。。。。。o(╥﹏╥)o";
            }
        };
    }
}
