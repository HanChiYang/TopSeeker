package com.topseeker.act.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;


@Service("actService")
public class ActService {

	@Autowired
	ActRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	

	

	public void addAct(ActVO actVO) {
		repository.save(actVO);
	}

	public void updateAct(ActVO actVO) {
		repository.save(actVO);
	}

	public void deleteAct(Integer actNo) {
		if (repository.existsById(actNo))
			repository.deleteByActNo(actNo);

	}

	public ActVO getOneAct(Integer actNo) {
		Optional<ActVO> optional = repository.findById(actNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}

	public List<ActVO> getAll() {
		return repository.findAll();
	}

	public List<ActVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Act.getAllC(map,sessionFactory.openSession());
	}
	

}