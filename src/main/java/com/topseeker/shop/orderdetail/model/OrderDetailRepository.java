package com.topseeker.shop.orderdetail.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailVO, CompositeDetail>{
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_order_detail WHERE order_no =?1 and prod_no =?2", nativeQuery = true)
	void deleteByCompositeDetail(Integer orderNo, Integer prodNo);
	
//	@Query(value = "from OrderDetailVO where orderNo=?1 and memNo like?2 and saleNo=?3 and orderDate=?4 and orderAmount=?5 and orderShipment=?6 
//					and orderDiscount=?7 and orderBill=?8 and orderName=?9  and orderPhone=?10  and orderAddress=?11  and orderStatus=?12 
//					and paymentStatus=?13 and paymentMethod=?14 and deliveryMethod=?15 and remarks=?16 order by orderNo")
//	List<SaleVO> findByOthers(int orderNo, int memNo, int saleNo, java.util.Timestamp orderDate);

	
}
