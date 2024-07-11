package com.topseeker.shop.sale.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository extends JpaRepository<SaleVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_sale WHERE sale_no =?1", nativeQuery = true)
	void deleteBySaleNo(int saleNo);
	
	//複合查詢
//	@Query(value = "from SaleVO where saleNo=?1 and saleName like?2 order by saleNo")
//	List<SaleVO> findByOthers(int saleNo , String saleName);
	
}
