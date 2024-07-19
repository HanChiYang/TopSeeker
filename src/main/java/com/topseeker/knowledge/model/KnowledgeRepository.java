package com.topseeker.knowledge.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.info.model.ShopInfoVO;


public interface KnowledgeRepository  extends JpaRepository<KnowledgeVO, Integer>{
	
	//依文章編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from knowledge where know_no=?1", nativeQuery = true)
	void deleteByKnowNo(Integer knowNo);
	
	//自訂的條件查詢
	
	//自訂的條件查詢
	//取單一圖片
	@Query(value = "SELECT * FROM knowledge WHERE know_no=?1", nativeQuery = true)
	KnowledgeVO getKnowledgePic(Integer knowNo);
	
	//商城後台，透過最新消息編號更改最新消息上下架狀態
    @Transactional
    @Modifying
    @Query(value = "update knowledge set know_status = ?2 where know_no = ?1", nativeQuery = true)
    void updateKnowStatus(int knowNo, int knowStatus);
    
    // 前台新手知識頁面，取所有上架的新手知識，依新到舊排列
   	@Query(value = "SELECT * FROM knowledge WHERE know_status=1 ORDER BY know_publish_date DESC", nativeQuery = true)
   	List<KnowledgeVO> getAllReleasedKnow();
	
}
