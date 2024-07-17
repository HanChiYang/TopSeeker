package com.topseeker.shopinfo.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.shop.productpic.model.ShopProductPicVO;

@Service("shopInfoService")
public class ShopInfoService {

	@Autowired
	ShopInfoRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 新增
	public void addShopInfo(ShopInfoVO shopInfoVO) {
		repository.save(shopInfoVO);
	}

	// 修改
	public void updateShopInfo(ShopInfoVO shopInfoVO) {
		repository.save(shopInfoVO);
	}

	// 刪除
	public void deleteShopInfoVO(Integer infoNo) {
		if (repository.existsById(infoNo))
			repository.deleteByInfoNo(infoNo);
	}
	
	//取單一文章
	public ShopInfoVO getOneShopInfo(Integer infoNo) {
		Optional<ShopInfoVO> optional = repository.findById(infoNo);
		return optional.orElse(null);
		//有資料則回傳其值，沒有則回傳null
	}
	
	//取全數文章
	public List<ShopInfoVO> getAll() {
		return repository.findAll();
	}
	
	//取單一圖片
	public ShopInfoVO getShopInfoPic(Integer infoNo) {
		return repository.getShopInfoPic(infoNo);
	}
	

	
//	//複合搜尋
//	public List<ShopInfoVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_ShopInfoVO.getAllC(map, sessionFactory.openSession());
//	}
	
}