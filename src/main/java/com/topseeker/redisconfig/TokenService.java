package com.topseeker.redisconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.topseeker.member.model.MemberVO;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class TokenService {
	Gson gson = new Gson();

	@Autowired
	JedisPool jedisPool;

	public void saveToken(String token, Integer memNo, String memEmail) {
		try (Jedis jedis = jedisPool.getResource()) {
			MemberVO memberVO = new MemberVO(memNo, memEmail);
			// 轉成JSON格式字串
			String json = gson.toJson(memberVO);

			//將傳入的參數token作為key，參數作為值存入redis
			jedis.set(token, json);
			
			//設定180秒後刪除失效
			jedis.expire(token, 180);
		}
	}

	public MemberVO checkToken(String token) {
		try (Jedis jedis = jedisPool.getResource()) {
			String memberVO = jedis.get(token);
			return gson.fromJson(memberVO, MemberVO.class);
		}
	}

}
