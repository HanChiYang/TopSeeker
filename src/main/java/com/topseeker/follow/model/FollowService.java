package com.topseeker.follow.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service("followService")
public class FollowService {
	
	@Autowired
	FollowRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

    // 新增追隨者
	public void addFollow(Integer memNoA,Integer memNoB) {
		repository.addFollow(memNoA, memNoB);
	}

    // 刪除追隨者
	public void deleteFollow(Integer memNo,Integer beFollowedMemNo) {
		if (repository.existsById(memNo))
			repository.deleteByMemNo(memNo, beFollowedMemNo);
	}

	// 獲取所有追隨者
	public List<FollowVO> findAllFollowers(Integer memNo) {
		return repository.findAllFollowers(memNo);
	}


}
