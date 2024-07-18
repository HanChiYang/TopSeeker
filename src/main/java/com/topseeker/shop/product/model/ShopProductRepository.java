package com.topseeker.shop.product.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.producttype.model.ShopProductTypeVO;

public interface ShopProductRepository extends JpaRepository<ShopProductVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from shop_product where prod_no = ?1", nativeQuery = true)
	void deleteByProdNo(int prodNo);
	
	//自訂的條件查詢
	//透過商品類別及部分名稱模糊搜尋，並依商品上架狀態篩選，依上架日期排序
	@Query(value = "from ShopProductVO where prod_type_no = ?1 and prod_name like?2 and prod_status = ?3 order by prod_date")
	List<ShopProductVO> findByOthers(int prodTypeNo, String prodName, int prodStatus);
	
	//商城分類用:篩選已上架的商品，依上架日期排序
	@Query(value = "SELECT * FROM shop_product WHERE prod_status = 1 ORDER BY prod_date DESC", nativeQuery = true)
	List<ShopProductVO> getAllReleasedProd();
	
	//商城分類用:篩選已上架的商品，並透過商品類別篩選，依上架日期排序
	@Query(value = "SELECT * FROM shop_product WHERE prod_type_no = ?1 AND prod_status = 1 ORDER BY prod_date DESC", nativeQuery = true)
	List<ShopProductVO> findByProdTypeNo(int prodTypeNo);
	
	//商城後台，透過商品編號更改商品上下架狀態
    @Transactional
    @Modifying
    @Query(value = "update shop_product set prod_status = ?2 where prod_no = ?1", nativeQuery = true)
    void updateProdStatus(int prodNo, int prodStatus);
	
	
}
