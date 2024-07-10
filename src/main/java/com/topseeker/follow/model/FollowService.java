package com.topseeker.follow.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.message.model.MessageRepository;
import com.topseeker.message.model.MessageVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Follow;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Message;
 
@Service("followService")
public class FollowService {
	
	@Autowired
	FollowRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addFollow(FollowVO followVO) {
		repository.save(followVO);
	}

	public void updateFollow(FollowVO followVO) {
		repository.save(followVO);
	}

	public void deleteFollow(Integer memNoA,Integer memNoB) {
		if (repository.existsById(memNoA))
			repository.deleteByMemNoA(memNoA,memNoB);
//		    repository.deleteById(actPartNo);
	}

	public FollowVO getOneFollow(Integer memNoA) {
		Optional<FollowVO> optional = repository.findById(memNoA);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<FollowVO> getAll() {
		return repository.findAll();
	}

	public List<FollowVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Follow.getAllC(map,sessionFactory.openSession());
	}

}
