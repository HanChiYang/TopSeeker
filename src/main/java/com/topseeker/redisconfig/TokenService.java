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
			System.out.println(json);

			jedis.set(token, json);
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
