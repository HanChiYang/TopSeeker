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
	
	//取單一商品(商品圖片用)
	public ShopProductVO getOneShopProduct(Integer prodNo) {
		Optional<ShopProductVO> optional = repository.findById(prodNo);
		return optional.orElse(null);
		//有資料則回傳其值，沒有則回傳null
	}
	
	//取全數商品
	public List<ShopProductVO> getAll() {
		return repository.findAll();
	}
	
	//取全數【已上架】商品
	public List<ShopProductVO> getAllReleasedProd() {
		return repository.getAllReleasedProd();
	}
	
	//依【商品類別】取全數商品
	public List<ShopProductVO> findByProdTypeNo(int prodTypeNo) {
		return repository.findByProdTypeNo(prodTypeNo);
	}
	
	//商城搜尋用:依搜尋列輸入的文字，搜尋商品名稱或商品資訊中有類似的關鍵字(模糊搜尋)，依上架日期排序
	public List<ShopProductVO> findByProdNameOrInfo(String keyword) {
		return repository.findByProdNameOrInfo(keyword);
	}
	
	
	//後台商品的複合搜尋
	public List<ShopProductVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ShopProduct.getAllC(map, sessionFactory.openSession());
	}
	
	// 後台依【商品編號】，更改商品狀態
    public void updateProdStatus(int prodNo, int prodStatus) {
        repository.updateProdStatus(prodNo, prodStatus);
    }
	
}
