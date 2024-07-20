package com.topseeker.shop.info.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.productpic.model.ShopProductPicVO;


public interface ShopInfoRepository  extends JpaRepository<ShopInfoVO, Integer>{
	
	//依文章編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from shop_info where info_no=?1", nativeQuery = true)
	void deleteByInfoNo(Integer infoNo);
	
	//自訂的條件查詢
	//取單一圖片
	@Query(value = "SELECT * FROM shop_info WHERE info_no=?1", nativeQuery = true)
	ShopInfoVO getShopInfoPic(Integer infoNo);
	
	//商城後台，透過最新消息編號更改最新消息上下架狀態
    @Transactional
    @Modifying
    @Query(value = "update shop_info set info_status = ?2 where info_no = ?1", nativeQuery = true)
    void updateInfoStatus(int infoNo, int infoStatus);
    
 // 前台最新消息頁面，取所有上架的最新消息，依新到舊排列
	@Query(value = "SELECT * FROM shop_info WHERE info_status=1 ORDER BY info_date DESC", nativeQuery = true)
	List<ShopInfoVO> getAllReleasedInfo();
}