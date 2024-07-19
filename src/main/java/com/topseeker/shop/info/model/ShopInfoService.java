package com.topseeker.shop.info.model;

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
	
	// 後台依【最新消息編號】，更改最新消息狀態
    public void updateInfoStatus(int infoNo, int infoStatus) {
        repository.updateInfoStatus(infoNo, infoStatus);
    }
    
    // 前台最新消息頁面，取所有上架的最新消息，依新到舊排列
    public List<ShopInfoVO> getAllReleasedInfo() {
    	return repository.getAllReleasedInfo();
    }

	
}