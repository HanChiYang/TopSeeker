package com.topseeker.shop.sale.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import hibernate.util.HibernateUtil_CompositeQuery_sale;

@Service
public class SaleService {
	@Autowired
	SaleRepository repository;

	
	public void addSale(SaleVO saleVO) {
		repository.save(saleVO);
	}
	
	public void updateSale(SaleVO saleVO) {
		repository.save(saleVO);
	}
	
	public void deleteSale(Integer saleNo) {
		if(repository.existsById(saleNo)) {
			repository.deleteBySaleNo(saleNo);
		}
	}
	
	public SaleVO getOneSale(Integer saleNo) {
		Optional<SaleVO> optional = repository.findById(saleNo);
		return optional.orElse(null);
	}
	
	public List<SaleVO> getAll(){
		return repository.findAll();
	}
	
	public List<SaleVO> getApplicableSales(Integer totalAmount, Timestamp currentTime){
		
		return repository.findByMinimumAmountLessThanEqual(totalAmount,currentTime);
	}
	
	//尋找期間內的優惠活動
	public List<SaleVO> getCurrentSales() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return repository.findCurrentSales(now);
		
	}
	
	//檢查被使用的優惠
	  public boolean isSaleUsed(Integer saleNo) {
	        
	        return repository.isSaleUsed(saleNo);
	}
}
