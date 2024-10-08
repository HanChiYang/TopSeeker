package com.topseeker.shop.sale.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SaleRepository extends JpaRepository<SaleVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_sale WHERE sale_no =?1", nativeQuery = true)
	void deleteBySaleNo(int saleNo);
	
	//顯示期間內的折扣
	@Query("SELECT s FROM SaleVO s WHERE :now BETWEEN s.saleStdate AND s.saleEddate ORDER BY s.saleNo")
    List<SaleVO> findCurrentSales(@Param("now") Timestamp now);
//	
	@Query("SELECT s FROM SaleVO s "
			+ "WHERE :totalAmount >= s.saleAmount AND :currentTime Between s.saleStdate AND s.saleEddate"
			)
	List<SaleVO> findByMinimumAmountLessThanEqual(Integer totalAmount, Timestamp currentTime);
	
	
//	 @Query("SELECT s FROM SaleVO s "
//	            + "WHERE :totalAmount >= s.saleAmount AND :currentTime BETWEEN s.saleStdate AND s.saleEddate "
//	            + "AND s.discountAmount = (SELECT MIN(s2.discountAmount) FROM SaleVO s2 "
//	            + "WHERE :totalAmount >= s2.saleAmount AND :currentTime BETWEEN s2.saleStdate AND s2.saleEddate)")
//	    Optional<SaleVO> findBestDiscount(@Param("totalAmount") Integer totalAmount, @Param("currentTime") Timestamp currentTime);
	
	//刪除前檢查該優惠是否被使用
	@Query("SELECT COUNT(o) > 0 FROM OrderVO o WHERE o.saleVO.saleNo = :saleNo")
    boolean isSaleUsed(@Param("saleNo") Integer saleNo);
	
}
