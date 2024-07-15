package com.topseeker.shop.wishlist.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ShopWishlistRepository  extends JpaRepository<ShopWishlistVO, Integer>{
	
	//依收藏編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from shop_wishlist where wishlist_no=?1", nativeQuery = true)
	void deleteByProdNo(Integer wishlistNo);
	
	//自訂的條件查詢
	
	//透過會員編號，搜尋【所有】已收藏的商品編號
	@Query(value = "select * from shop_wishlist where mem_no = ?1", nativeQuery = true)
	List<ShopWishlistVO> findByMemNo(Integer memNo);
	
	//透過會員編號，搜尋【單一】已收藏的商品編號
	@Query(value = "select * from shop_wishlist where mem_no = ?1 AND prod_no = ?2", nativeQuery = true)
	ShopWishlistVO findByMemNoAndProdNo(Integer memNo, Integer prodNo);
	
	
	
	
}