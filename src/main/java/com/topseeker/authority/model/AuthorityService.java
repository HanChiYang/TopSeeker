package com.topseeker.authority.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("AuthorityService")
public class AuthorityService {

	@Autowired
	AuthorityRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public List<AuthorityVO> getAll() {
		return repository.findAll();
	}

}