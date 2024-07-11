package com.topseeker.shop.orderdetail.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("orderDetailService")
public class OrderDetailService {
	@Autowired
	OrderDetailRepository repository;
	
//	@Autowired
//	private SessionFactory sessionFactory;
	
	public OrderDetailVO addOrderDetail(OrderDetailVO orderDetailVO) {
		return repository.save(orderDetailVO);
	}
	
	public void updateOrderDetail(OrderDetailVO orderDetailVO) {
		 CompositeDetail compositeDetail = orderDetailVO.getCompositeDetail();
		    OrderDetailVO existingOrderDetail = repository.findById(compositeDetail)
		            .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

		    existingOrderDetail.setOrderQty(orderDetailVO.getOrderQty());
		    existingOrderDetail.setOrderPrice(orderDetailVO.getOrderPrice());

		    repository.save(existingOrderDetail);
	}
	
	
	public OrderDetailVO getOneOrderDetail(CompositeDetail compositeDetail) {
		Optional<OrderDetailVO> optional = repository.findById(compositeDetail);
		return optional.orElse(null);
	}
	

	public void deleteOrderDetail(CompositeDetail compositeDetail) {
		try {
			if (repository.existsById(compositeDetail)) 
				repository.deleteByCompositeDetail(compositeDetail.getOrderNo(), compositeDetail.getProdNo());
		 } catch(Exception e) {
	        e.printStackTrace();
	    
	    }
	}
	
	public List<OrderDetailVO> getAll(){
		return repository.findAll();
	}
	

}
