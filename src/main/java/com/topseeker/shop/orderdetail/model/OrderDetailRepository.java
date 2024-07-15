package com.topseeker.shop.orderdetail.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailVO, CompositeDetail>{
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_order_detail WHERE order_no =?1 and prod_no =?2", nativeQuery = true)
	void deleteByCompositeDetail(Integer orderNo, Integer prodNo);
	
	
	@Query("SELECT od FROM OrderDetailVO od WHERE od.orderVO.orderNo = :orderNo")
	List<OrderDetailVO> findOrderDetailsByOrderNo(Integer orderNo);
	
}
