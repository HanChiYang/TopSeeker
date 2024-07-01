package com.topseeker.redisconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//@Service
public class TokenServiceSpringBoot {

//    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 保存临时token并设置过期时间为3分钟
    public void saveToken(String key, String token) {
        redisTemplate.opsForValue().set(key, token, 3, TimeUnit.MINUTES);
    }

    // 获取临时token
    public String getToken(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 删除token
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
}

