// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tourOrder.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


//<EmpVO, Integer> 這裡的Integer 是主鍵
@Transactional
public interface TourOrderRepository extends JpaRepository<TourOrderVO, Integer> {

	@Modifying
	@Query(value = "delete from tour_order where order_no =?1", nativeQuery = true)
	void deleteByOrderNo(int empNo);

	//● (自訂)條件查詢
	@Query(value = "from TourOrderVO where orderNo=?1 and memNo=?2 and groupNo=?3 order by orderNo")
	List<TourOrderVO> findByOthers(Integer orderNo , Integer memNo , Integer groupNo);
	
	
	@Query(value = "select * from tour_order where mem_no=?1 order by order_date", nativeQuery = true)
    List<TourOrderVO> findByMemberNoAndOrderDate(Integer memNo);

	
	@Query(value = "select * from tour_order where mem_no=?1 and order_date >= ?2 order by order_date", nativeQuery = true)
    List<TourOrderVO> findByMemberNoAndOrderDateAfter(Integer memNo, LocalDate startDate);
	
	@Modifying
	@Query(value = "INSERT INTO tour_order (mem_no, group_no, order_nums, order_pay, order_status, order_date, order_price, departure_date) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    void insertByGOO(Integer memNo, Integer groupNo, Integer orderNums, Byte orderPay, Byte orderStatus ,Date orderDate, Integer orderPrice, Date departureDate);
	
	
}