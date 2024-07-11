package com.topseeker.news.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_News;


@Service("newsService")
public class NewsService {

	@Autowired
	NewsRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addNews(NewsVO newsVO) {
		repository.save(newsVO);
	}

	public void updateNews(NewsVO newsVO) {
		repository.save(newsVO);
	}

	public void deleteNews(Integer newsNo) {
		if (repository.existsById(newsNo))
			repository.deleteByNewsNo(newsNo);
	}

	public NewsVO getOneNews(Integer newsNo) {
		Optional<NewsVO> optional = repository.findById(newsNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}

	public List<NewsVO> getAll() {
		return repository.findAll();
	}

	public List<NewsVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_News.getAllC(map,sessionFactory.openSession());
	}

}