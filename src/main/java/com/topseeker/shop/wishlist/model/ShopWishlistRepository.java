package com.topseeker.shop.wishlist.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.wishlist.model.ShopWishlistVO.CompositeWishlist;


public interface ShopWishlistRepository  extends JpaRepository<ShopWishlistVO, ShopWishlistVO.CompositeWishlist>{
	@Transactional
	@Modifying
	@Query(value = "delete from shop_wishlist where mem_no = ?1 and prod_no=?2", nativeQuery = true)
	void deleteByMemNoAndProdNo(Integer memNo, Integer prodNo);
}