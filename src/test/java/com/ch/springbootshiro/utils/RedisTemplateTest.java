package com.ch.springbootshiro.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableConfigurationProperties
public class RedisTemplateTest {
    //    @Autowired
//    private RedisTemplate<Object,Object> redisTemplate;
//    @Test
//    public void test (){
//        redisTemplate.opsForValue().set("a","1");
//
//    }
    @Value("${spring.redis.cluster.nodes}")
    public String redis;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Test
    public void getApplicationPeroperies() {
        System.out.println(redis);
        redisTemplate.opsForValue().set("a","1");
        System.out.println(redisTemplate.opsForValue().get("a"));
    }

    public String getRedis() {
        return redis;
    }

    public void setRedis(String redis) {
        this.redis = redis;
    }
}
