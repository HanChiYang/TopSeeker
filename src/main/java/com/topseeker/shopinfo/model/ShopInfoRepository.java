package com.topseeker.shopinfo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ShopInfoRepository  extends JpaRepository<ShopInfoVO, Integer>{
	
	//依文章編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from shop_info where info_no=?1", nativeQuery = true)
	void deleteByInfoNo(Integer infoNo);
	
	//自訂的條件查詢
	
}