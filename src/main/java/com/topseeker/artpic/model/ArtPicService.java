package com.topseeker.artpic.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_ArtPic;

@Service("artpicService")
public class ArtPicService {
	
	@Autowired
	ArtPicRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addArtPic(ArtPicVO artpicVO) {
		repository.save(artpicVO);
	}

	public void updateArtPic(ArtPicVO artpicVO) {
		repository.save(artpicVO);
	}

	public void deleteArtPic(Integer artPicNo) {
		if (repository.existsById(artPicNo))
			repository.deleteByArtPicNo(artPicNo);
//		    repository.deleteById(empno);
	}

	public ArtPicVO getOneArtPic(Integer artPicNo) {
		Optional<ArtPicVO> optional = repository.findById(artPicNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public ArtPicVO getOneArtNo(Integer artNo) {
		Optional<ArtPicVO> optional = repository.findById(artNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArtPicVO> getAll() {
		return repository.findAll();
	}

	public List<ArtPicVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ArtPic.getAllC(map,sessionFactory.openSession());
	}

}
