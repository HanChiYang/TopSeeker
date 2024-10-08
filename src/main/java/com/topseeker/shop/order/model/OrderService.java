package com.topseeker.shop.order.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.Notification;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.notification.model.NotificationRepository;
import com.topseeker.shop.orderdetail.model.OrderDetailRepository;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Order;



@Service("orderService")
public class OrderService {
	@Autowired
	OrderRepository repository;
	
	@Autowired
	OrderDetailRepository odrepository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	NotificationRepository notirepository;
	
	
	@Transactional
	public void addOrderWithOrderDetail(OrderVO orderVO) {		
		
		repository.save(orderVO);
		
        for (OrderDetailVO detail : orderVO.getOrderDetails()) {
            detail.setOrderVO(orderVO);
            odrepository.save(detail);
        }
       
	}

	@Transactional
	public void updateOrder(OrderVO orderVO) {
		
        Optional<OrderVO> existingOrderOpt = repository.findById(orderVO.getOrderNo());
        if (existingOrderOpt.isPresent()) {
            OrderVO existingOrder = existingOrderOpt.get();
            existingOrder.setOrderName(orderVO.getOrderName());
            existingOrder.setOrderPhone(orderVO.getOrderPhone());
            existingOrder.setOrderAddress(orderVO.getOrderAddress());
            existingOrder.setOrderStatus(orderVO.getOrderStatus());
            existingOrder.setPaymentStatus(orderVO.getPaymentStatus());
            existingOrder.setRemarks(orderVO.getRemarks());
                       
            repository.save(existingOrder);
        }
	}
	
	
	public void deleteOrder(Integer orderNo) {
		if (repository.existsById(orderNo))
			repository.deleteByOrderNo(orderNo);
//		    repository.deleteById(empno);
	}
	
	public OrderVO getOneOrder(Integer orderNo) {
		Optional<OrderVO> optional = repository.findById(orderNo);
		return optional.orElse(null);
	}
	
	public List<OrderVO> getAll(){
		return repository.findAll();
	}
	
	public List<OrderVO> getMemAllOrder(Integer memNo){
		return repository.findByMem(memNo);
		
	} 
	

	public List<OrderVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Order.getAllC(map,sessionFactory.openSession());
	}
	
}
