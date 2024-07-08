package com.topseeker.shop.producttype.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shopProductTypeService")
public class ShopProductTypeService {

	@Autowired
	ShopProductTypeRepository repository;

	@Autowired
	private SessionFactory sessionFactory;
	
	

	// 新增
	public void addShopProductType(ShopProductTypeVO shopProductTypeVO) {
		repository.save(shopProductTypeVO);
	}

	// 修改
	public void updateShopProductType(ShopProductTypeVO shopProductTypeVO) {
		repository.save(shopProductTypeVO);
	}

	// 刪除
	public void deletShopProductType(Integer prodTypeNo) {
		if (repository.existsById(prodTypeNo))
			repository.deleteByProdNo(prodTypeNo);
	}
	
	//取單一產品
	public ShopProductTypeVO getOneShopProductType(Integer prodTypeNo) {
		Optional<ShopProductTypeVO> optional = repository.findById(prodTypeNo);
		return optional.orElse(null);
		//有資料則回傳其值，沒有則回傳null
	}
	
	//取全數產品
	public List<ShopProductTypeVO> getAll() {
		return repository.findAll();
	}
	
//	public List<ShopProductVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_ShopProduct.geAllC(map, sessionFactory.opendSession());
//	}
	
	
}
