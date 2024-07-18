package com.topseeker.shop.productpic.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ShopProductPicService")
public class ShopProductPicService {

	@Autowired
	ShopProductPicRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 新增
	public void addShopProductPic(ShopProductPicVO shopProductPicVO) {
		repository.save(shopProductPicVO);
	}

	// 修改
	public void updateShopProductPic(ShopProductPicVO shopProductPicVO) {
		repository.save(shopProductPicVO);
	}

	// 刪除
	public boolean deleteShopProductPic(Integer prodPicNo) {
		if (repository.existsById(prodPicNo)) {
			repository.deleteByProdPicNo(prodPicNo);
			return true;
		} else {
			return false;
		}
	}

	// 取單一圖片
	public List<ShopProductPicVO> getOneShopProductPic(Integer prodNo) {

		List<ShopProductPicVO> optional = repository.getShopProductPicVOList(prodNo);
		if (optional.isEmpty())
			return null;

		return optional;
		// 有資料則回傳其值，沒有則回傳null
	}

	// 只取第一張圖片(測試用)
	public ShopProductPicVO getFirstShopProductPicVO(Integer prodNo) {
		return repository.getFirstShopProductPicVO(prodNo);
		// 有資料則回傳其值，沒有則回傳null
	}

	// 取單一商品全數圖片用
	public List<ShopProductPicVO> getAllProductPic(Integer prodPicNo) {
		return repository.getAllProductPic(prodPicNo);
	}

	// 取全數圖片
	public List<ShopProductPicVO> getAll() {
		return repository.findAll();
	}

	public List<ShopProductPicVO> getShopProductPicVOList(Integer prodNo) {
		return repository.getShopProductPicVOList(prodNo);
	}

//	public List<ShopProductPicVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_ShopProductPic.geAllC(map, sessionFactory.opendSession());
//	}

}
