package com.topseeker.artpic.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import javax.validation.Valid;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.article.model.ArticleVO;

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

	public Set<ArtPicVO> saveArtPics(MultipartFile[] artPics, @Valid ArticleVO articleVO) {
		Set<ArtPicVO> artPicVOs = new HashSet<>();
		for (MultipartFile file : artPics) {
			try {
				ArtPicVO artPicVO = new ArtPicVO();
				artPicVO.setArticleVO(articleVO);
				artPicVO.setArtPic(file.getBytes());
				artPicVOs.add(artPicVO);
				repository.save(artPicVO);
			} catch (IOException e) {
				e.printStackTrace();
				// 可以根据需要添加更多错误处理逻辑
			}
		}
		return artPicVOs;
	}



}
