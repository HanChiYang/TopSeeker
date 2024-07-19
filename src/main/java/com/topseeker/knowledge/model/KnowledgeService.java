package com.topseeker.knowledge.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.shop.info.model.ShopInfoVO;

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
	
	//取單一圖片
	public KnowledgeVO getKnowledgePic(Integer infoNo) {
		return repository.getKnowledgePic(infoNo);
	}
	
	// 後台依【新手知識編號】，更改最新消息狀態
    public void updateKnowStatus(int knowNo, int knowStatus) {
        repository.updateKnowStatus(knowNo, knowStatus);
    }
    
  
    // 前台新手知識頁面，取所有上架的知識，依新到舊排列
    public List<KnowledgeVO> getAllReleasedKnow() {
    	return repository.getAllReleasedKnow();
    }
	
}