package com.topseeker.shop.productpic.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ShopProductPicRepository  extends JpaRepository<ShopProductPicVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from shop_product_pic where prod_pic_no = ?1", nativeQuery = true)
	void deleteByProdPicNo(int prodPicNo);
	
	
	@Query(value = "from ShopProductPicVO where prod_No= ?1")
	List<ShopProductPicVO> getShopProductPicVOList(Integer prodNo);
	
	@Query(value = "SELECT * FROM shop_product_pic WHERE prod_no=?1 limit 1", nativeQuery = true)
	ShopProductPicVO getFirstShopProductPicVO(Integer prodNo);
	
	//拿取單一商品的全部圖片
	@Query(value = "SELECT * FROM shop_product_pic WHERE prod_pic_no=?1", nativeQuery = true)
	List<ShopProductPicVO> getAllProductPic(Integer prodPicNo);
}