package com.zjk.hy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class PageController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "findStr")
    public String findStr(String id){
        //redisTemplate.opsForValue().set("amount",10000);
        String amount = stringRedisTemplate.opsForValue().get("amount");
        System.out.println(amount);
        return stringRedisTemplate.opsForValue().get("amount") + "";
    }
    @GetMapping(value = "setStr")
    public String setStr(String id){
        redisTemplate.opsForValue().set("amount","10000");
        String amount = (String) redisTemplate.opsForValue().get("amount");
        System.out.println(amount);
        return redisTemplate.opsForValue().get("amount") + "";
    }

    @GetMapping(value = "luascript")
    public String luascript(String id){
        // lua脚本对象
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        // 加载lua脚本文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/get.lua")));
        // 指定返回类型
        redisScript.setResultType(Integer.class);
        // 执行lua脚本
        String key = "a";
        String res = (String) redisTemplate.execute(redisScript,Arrays.asList(key));
        System.out.println(res);
        return res;
    }
    @GetMapping(value = "doTaskCas")
    public  String doTaskCas(){
        ExecutorService service = Executors.newFixedThreadPool(6);
        for(int i=0;i<100;i++) {
            service.submit(()->handleAmountCAS());
        }
        return "bbbbbbbbbbbbbb";
    }

    @GetMapping(value = "doTask")
    public  String doTask(){
        ExecutorService service = Executors.newFixedThreadPool(6);
        for(int i=0;i<100;i++) {
            service.submit(()->handleAmount());
        }
        return "aaa";
    }

    public void handleAmount() {
        String key = "amount";
        String amount = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.opsForValue().set(key,(Integer.valueOf(amount)-100)+"");
    }

    public void handleAmountCAS() {
        while (true) {
            String key = "amount";
            // lua脚本对象
            DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
            // 加载lua脚本文件
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/handleAmount.lua")));
            // 指定返回类型
            redisScript.setResultType(Boolean.class);
            String amount = (String) redisTemplate.opsForValue().get(key);
            //Long amount = Long.valueOf(res);
            Boolean flag = (Boolean) redisTemplate.execute(redisScript, Arrays.asList(key), amount, (Integer.valueOf(amount) - 100)+"");
            if (flag) break;
        }
    }

    @GetMapping(value = "luaStr")
    public Integer luaStr(){
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>("return redis.call('get',KEYS[1])+100");
        redisScript.setResultType(Integer.class);
        // 执行lua脚本
        List<String> keyList = new ArrayList();
        keyList.add("amount");
        Integer values = (Integer) redisTemplate.execute(redisScript,keyList);
        return values;
    }

}
