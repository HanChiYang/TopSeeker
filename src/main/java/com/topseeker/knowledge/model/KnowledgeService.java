package com.topseeker.knowledge.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("knowledgeService")
public class KnowledgeService {

	@Autowired
	KnowledgeRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 新增
	public void addKnowledge(KnowledgeVO knowledgeVO) {
		repository.save(knowledgeVO);
	}

	// 修改
	public void updateKnowledge(KnowledgeVO knowledgeVO) {
		repository.save(knowledgeVO);
	}

	// 刪除
	public void deleteKnowledgeVO(Integer knowNo) {
		if (repository.existsById(knowNo))
			repository.deleteByKnowNo(knowNo);
	}
	
	//取單一文章
	public KnowledgeVO getOneKnowledge(Integer knowNo) {
		Optional<KnowledgeVO> optional = repository.findById(knowNo);
		return optional.orElse(null);
		//有資料則回傳其值，沒有則回傳null
	}
	
	//取全數文章
	public List<KnowledgeVO> getAll() {
		return repository.findAll();
	}
	
//	//複合搜尋
//	public List<KnowledgeVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_KnowledgeVO.getAllC(map, sessionFactory.openSession());
//	}
	
}