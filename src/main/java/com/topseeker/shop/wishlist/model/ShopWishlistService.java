package com.topseeker.shop.wishlist.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("ShopWishlistService")
public class ShopWishlistService {

	@Autowired
	ShopWishlistRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	// 新增
    public void addShopWishlist(ShopWishlistVO shopWishlistVO) {
        repository.save(shopWishlistVO);
    }
	
	// 刪除
	public void deletShopWishlist(Integer wishlistNo) {
		repository.deleteById(wishlistNo);
	}
    
    //透過會員號碼查詢【全部】收藏商品
    public List <ShopWishlistVO> showMemWishlist(Integer memNo) {
    	return repository.findByMemNo(memNo);
    }
    
    //透過會員號碼查詢【單一】收藏商品
    public ShopWishlistVO findByMemNoAndProdNo(Integer memNo, Integer prodNo) {
        return repository.findByMemNoAndProdNo(memNo, prodNo);
    }
    
    
    //============架構變更，故不使用複合主鍵============
    
    //此複合主鍵無須修改，故註解
//	// 修改
//	public void updateShopWishlist(ShopWishlistVO shopWishlistVO) {
//		repository.save(shopWishlistVO);
//	}
    
//    // 據複合主鍵查詢
//    public ShopWishlistVO getShopWishlist(Integer memNo, Integer prodNo) {
//        CompositeWishlist wishlistKey = new CompositeWishlist(memNo, prodNo);
//        return repository.findById(wishlistKey).orElse(null);
//		//有資料則回傳其值，沒有則回傳null
//    }
	
}
