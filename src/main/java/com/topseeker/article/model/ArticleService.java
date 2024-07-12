package com.topseeker.article.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.artpic.model.ArtPicRepository;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Article;


@Service("articleService")
public class ArticleService {
	

	@Autowired
	ArticleRepository repository;
	
	@Autowired
    private ArtPicRepository artPicRepository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addArticle(ArticleVO articleVO) {
		repository.save(articleVO);
	}

	public void updateArticle(ArticleVO articleVO) {
		repository.save(articleVO);
	}
	@Transactional
	public void deleteArticle(Integer artNo) {
		 
		ArticleVO article = repository.findById(artNo).orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + artNo));

	       
		artPicRepository.deleteByArticleVO(article);
		if (repository.existsById(artNo))
			repository.deleteByActMsgNo(artNo);
//		    repository.deleteById(actPartNo);
	}

	public ArticleVO getOneArticle(Integer artNo) {
		  Optional<ArticleVO> optional = repository.findById(artNo);
		    ArticleVO articleVO = optional.orElse(null);
		    // 添加日志输出
		    if (articleVO != null) {
		        System.out.println("Found Article: " + articleVO.getArtTitle());
		    } else {
		        System.out.println("No Article found with artNo: " + artNo);
		    }
		    return articleVO;  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArticleVO> getAll() {
        return repository.findByArtStatusNot(0);
	}

	public List<ArticleVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Article.getAllC(map, sessionFactory.openSession())
                .stream().filter(article -> article.getArtStatus() != 0).collect(Collectors.toList());	}

	public ArticleVO findById(Integer artNo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	  public List<ArticleVO> getAllIncludingStatusZero() {
	        return repository.findAll();
	    }
	  
	  public void updateArticleStatus(Integer artNo, Integer newStatus) {
	        ArticleVO article = repository.findById(artNo).orElseThrow(() -> new RuntimeException("Article not found"));
	        article.setArtStatus(newStatus);
	        repository.save(article);
	    }
	
	

}
