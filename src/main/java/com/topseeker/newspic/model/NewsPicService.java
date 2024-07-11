package com.topseeker.newspic.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;


@Service("newsPicService")
public class NewsPicService {

	@Autowired
	NewsPicRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addNewsPic(NewsPicVO newsPicVO) {
		repository.save(newsPicVO);
	}

	public void updateNewsPic(NewsPicVO newsPicVO) {
		repository.save(newsPicVO);
	}

	public void deleteNewsPic(Integer newsImgNo) {
		if (repository.existsById(newsImgNo))
			repository.deleteByNewsImgNo(newsImgNo);
	}

	public NewsPicVO getOneNewsPic(Integer newsImgNo) {
		Optional<NewsPicVO> optional = repository.findById(newsImgNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}

	public List<NewsPicVO> getAll() {
		return repository.findAll();
	}

//	public List<ActVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Act.getAllC(map,sessionFactory.openSession());
//	}

}