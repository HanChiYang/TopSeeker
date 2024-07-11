package com.topseeker.artcomment.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.artcomment.model.ArtCommentRepository;
import com.topseeker.artcomment.model.ArtCommentVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_ArtComment;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Article;

@Service("artcommentService")
public class ArtCommentService {
	
	@Autowired
	ArtCommentRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addArtComment(ArtCommentVO artcommentVO) {
		
		if (artcommentVO.getArticleVO() == null) {
	        throw new IllegalArgumentException("artNo cannot be null");
	    }
		repository.save(artcommentVO);
	}

	public void updateArtComment(ArtCommentVO artcommentVO) {
		repository.save(artcommentVO);
	}

	public void deleteArtComment(Integer commentNo) {
		if (repository.existsById(commentNo))
			repository.deleteByCommentNo(commentNo);
//		    repository.deleteById(actPartNo);
	}

	public ArtCommentVO getOneArtComment(Integer commentNo) {
		Optional<ArtCommentVO> optional = repository.findById(commentNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArtCommentVO> getAll() {
		return repository.findAll();
	}

	public List<ArtCommentVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ArtComment.getAllC(map,sessionFactory.openSession());
	}
	
	

}
