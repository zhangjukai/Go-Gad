package com.zjk.hy.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zjk.hy.dao.UserDao;
import com.zjk.hy.dto.UserDo;
import com.zjk.hy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    //@HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")  //3秒钟以内就是正常的业务逻辑    })
    @Override
    @HystrixCommand(fallbackMethod = "findListFallBack",
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    @Transactional
    public List<UserDo> findList() {
        //throw new RuntimeException("卧槽，我出错啦");
       /* try {
            TimeUnit.SECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return userDao.findList();
    }

    public List<UserDo> findListFallBack() {
        System.out.println("进入服务降级回调方法");
        return null;
    }

}
