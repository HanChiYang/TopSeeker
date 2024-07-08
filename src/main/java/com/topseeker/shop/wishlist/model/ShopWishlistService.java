package com.topseeker.shop.wishlist.model;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.shop.wishlist.model.ShopWishlistVO.CompositeWishlist;

@Service("ShopWishlistService")
public class ShopWishlistService {

	@Autowired
	ShopWishlistRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	// 新增
    public void addShopWishlist(Integer memNo, Integer prodNo) {
        CompositeWishlist wishlistKey = new CompositeWishlist(memNo, prodNo);
        ShopWishlistVO shopwishlistVO = new ShopWishlistVO();
        shopwishlistVO.setCompositeWishlistKey(wishlistKey);
        repository.save(shopwishlistVO);
    }
	
    //此複合主鍵無須修改，故註解
//	// 修改
//	public void updateShopWishlist(ShopWishlistVO shopWishlistVO) {
//		repository.save(shopWishlistVO);
//	}
	
	// 刪除
	public void deletShopWishlist(Integer memNo, Integer prodNo) {
		repository.deleteByMemNoAndProdNo(memNo, prodNo);
	}
	
    // 據複合主鍵查詢
    public ShopWishlistVO getShopWishlist(Integer memNo, Integer prodNo) {
        CompositeWishlist wishlistKey = new CompositeWishlist(memNo, prodNo);
        return repository.findById(wishlistKey).orElse(null);
		//有資料則回傳其值，沒有則回傳null
    }

	
}
