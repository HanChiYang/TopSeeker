package com.topseeker.news.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

	@Autowired
    private JedisPool jedisPool;

    public void incrementViewCount(Integer newsNo) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "news:viewCount:" + newsNo;
            jedis.incr(key);
        }
    }

    public Integer getViewCount(Integer newsNo) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "news:viewCount:" + newsNo;
            String count = jedis.get(key);
            return count != null ? Integer.parseInt(count) : 0;
        }
    }
}
