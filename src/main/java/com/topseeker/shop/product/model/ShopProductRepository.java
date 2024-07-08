package com.topseeker.shop.product.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ShopProductRepository extends JpaRepository<ShopProductVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from shop_product where prod_no = ?1", nativeQuery = true)
	void deleteByProdNo(int prodNo);
	
	//自訂的條件查詢
	//透過商品類別及部分名稱模糊搜尋，並依商品上架狀態篩選，依上架日期排序
	@Query(value = "from ShopProductVO where prod_type_no = ?1 and prod_name like?2 and prod_status = ?3 order by prod_date")
	List<ShopProductVO> findByOthers(int prodTypeNo, String prodName, int prodStatus);
}
