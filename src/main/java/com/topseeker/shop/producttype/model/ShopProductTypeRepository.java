package com.topseeker.shop.producttype.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ShopProductTypeRepository  extends JpaRepository<ShopProductTypeVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from shop_product_type where prod_type_no = ?1", nativeQuery = true)
	void deleteByProdNo(int prodTypeNo);
}

