package com.topseeker.shop.product.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_ShopProduct;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;

@Service("shopProductService")
public class ShopProductService {

	@Autowired
	ShopProductRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 新增
	public void addShopProduct(ShopProductVO shopProductVO) {
		repository.save(shopProductVO);
	}

	// 修改
	public void updateShopProduct(ShopProductVO shopProductVO) {
		repository.save(shopProductVO);
	}

	// 刪除
	public void deletShopProduct(Integer prodNo) {
		if (repository.existsById(prodNo))
			repository.deleteByProdNo(prodNo);
	}
	
	//取單一產品(商品圖片用)
	public ShopProductVO getOneShopProduct(Integer prodNo) {
		Optional<ShopProductVO> optional = repository.findById(prodNo);
		return optional.orElse(null);
		//有資料則回傳其值，沒有則回傳null
	}
	
	//取全數產品
	public List<ShopProductVO> getAll() {
		return repository.findAll();
	}
	
	public List<ShopProductVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ShopProduct.getAllC(map, sessionFactory.openSession());
	}
	
}
