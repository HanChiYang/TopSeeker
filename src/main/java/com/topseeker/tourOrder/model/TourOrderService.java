package com.topseeker.tourOrder.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_TourOrder;
import com.topseeker.tourGroup.model.TourGroupVO;

@Service("tourOrderService")
public class TourOrderService {

	@Autowired
	TourOrderRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	  
	public void addOrder(TourOrderVO tourOrderVO) {
		repository.save(tourOrderVO);
	}

	public void updateOrder(TourOrderVO tourOrderVO) {
		repository.save(tourOrderVO);
	}

	public void deleteOrder(Integer orderNo) {
		if (repository.existsById(orderNo))
			repository.deleteByOrderNo(orderNo);
//		    repository.deleteById(empno);
	}

	public TourOrderVO getOneOrder(Integer orderNo) {
		Optional<TourOrderVO> optional = repository.findById(orderNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<TourOrderVO> getAll() {
		return repository.findAll();
	}
	
	
	public void insertByGOO(Integer memNo, Integer groupNo, Integer orderNums, Byte orderPay, Byte orderStatus, Date orderDate, Integer orderPrice) {
		repository.insertByGOO(memNo, groupNo, orderNums, orderPay, orderStatus, orderDate, orderPrice);
	}

	public List<TourOrderVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_TourOrder.getAllC(map,sessionFactory.openSession());
	}
	//會員的歷史訂單
	public List<TourOrderVO> getHistoricalOrders(Integer memNo) {
        return repository.findByMemberNoAndOrderDate(memNo);
    }

	//過濾功能
	public List<TourOrderVO> getOrdersWithinDays(Integer memNo, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return repository.findByMemberNoAndOrderDateAfter(memNo, startDate);
    }
	
	public boolean confirmOrder(Integer orderNo, byte orderPay) {
	    TourOrderVO tourOrderVO = getOneOrder(orderNo);
	    if (tourOrderVO != null) {
	        tourOrderVO.setOrderStatus((byte) 1); // 設置訂單狀態為已確認，但未支付
	        repository.save(tourOrderVO);
	        return true;
	    }
	    return false;
	}

	public void addComment(Integer orderNo, String orderComment) {
		TourOrderVO tourOrderVO = getOneOrder(orderNo);
        if (tourOrderVO != null) {
        	tourOrderVO.setOrderComment(orderComment);
        	repository.save(tourOrderVO);
        }
		
	}
	
	

	

}
	
	
	

