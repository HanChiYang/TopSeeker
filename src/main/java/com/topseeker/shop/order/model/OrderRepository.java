package com.topseeker.shop.order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.sale.model.SaleVO;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_order WHERE order_no =?1", nativeQuery = true)
	void deleteByOrderNo(int orderNo);

	@Query(value = "from OrderVO where orderNo=?1 and memNo=?2 and orderStatus=?3 and paymentStatus=?4 and paymentMethod=?5 and deliveryMethod=?6 order by orderNo")
	List<SaleVO> findByOthers(int orderNo, int memNo, int orderStatus, int paymentStatus, int paymentMethod,
			int deliveryMethod);

}

