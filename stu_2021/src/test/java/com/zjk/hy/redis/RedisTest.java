package com.zjk.hy.redis;

import com.zjk.hy.Application;
import com.zjk.hy.redis.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: zhangjukai
 * @CreateDate: 2021/9/27 10:44
 * @Description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
//@ActiveProfiles("prod")
public class RedisTest {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testSet() {
        redisUtil.set("aaa", "zhangjukai");
    }

    @Test
    public void testGet() {
        System.out.println(redisUtil.get("aaa"));
    }

    @Test
    public void testCacheSet(){
        Cache test_cache = cacheManager.getCache("TEST_CACHE");
        test_cache.put("zjk","zhangjukai");
    }

    @Test
    public void testCacheGet(){
        Cache test_cache = cacheManager.getCache("TEST_CACHE");
        System.out.println(test_cache.get("zjk").get());
    }

    @Test
    public void testLock(){
        RLock lock = redissonClient.getLock("aaaa");
        lock.lock();
        System.out.println("执行业务");
        lock.unlock();
    }

    @Test
    public void testTemplateGet(){
        System.out.println(stringRedisTemplate.opsForValue().get("aaa"));
    }

    @Test
    public void testTemplateSet() {
        redisTemplate.opsForValue().set("aaa","xiaoxinyun");
    }


}
