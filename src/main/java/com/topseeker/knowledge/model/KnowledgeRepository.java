package com.topseeker.knowledge.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface KnowledgeRepository  extends JpaRepository<KnowledgeVO, Integer>{
	
	//依文章編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from knowledge where know_no=?1", nativeQuery = true)
	void deleteByKnowNo(Integer knowNo);
	
	//自訂的條件查詢
	
}
