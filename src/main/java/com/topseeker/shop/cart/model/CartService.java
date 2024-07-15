package com.topseeker.shop.cart.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.shop.productpic.model.ShopProductPicVO;


@Service("cartService")
public class CartService {
	
	@Autowired
	CartRepository repository;
	
	@Transactional
	public CartVO  saveOrUpdateCart(CartVO cartVO) {
		Integer memNo = cartVO.getMemberVO().getMemNo();
		Integer prodNo = cartVO.getShopProductVO().getProdNo();
		
		
		CartVO existingCart = repository.findByMemberVO_MemNoAndShopProductVO_ProdNo(memNo, prodNo);
		 if (existingCart != null) {
//			repository.deleteByMemNoAndProdNo(memNo, prodNo);
			 existingCart.setProdQty(existingCart.getProdQty() + cartVO.getProdQty());
			 return repository.save(existingCart);
	        }

	        return repository.save(cartVO);
		
		
	}
	
	
	public List<CartVO> getMemAllList(Integer memNo){
		return repository.findByMemberVO_MemNo(memNo);
		
	}
	
	
	public CartVO getMemberCart(Integer memNo, Integer prodNo) {
	
		return repository.findByMemberVO_MemNoAndShopProductVO_ProdNo(memNo, prodNo);
	
	}
	
	public List<CartVO> getAll(){
		
		return repository.findAll();
		
	}
	
    public List<CartDetail> getCartDetail(Set<Integer> memNos, Map<Integer, Integer> details) {
        List<CartDetail> cartDetailList = repository.findCartDetail(memNos, details.keySet());
        for (CartDetail cartDetail : cartDetailList) {
            Integer memNo = cartDetail.getMemNo();
            if (details.containsKey(memNo)) {
                cartDetail.setProdQty(details.get(memNo));
            }
        }
        return cartDetailList;
    }
	
	
    @Transactional
    public void deleteByMemNoAndProdNo(Integer memNo, Integer prodNo) {
        repository.deleteByMemNoAndProdNo(memNo,prodNo);
    }
	
    @Transactional
    public void deleteMemAllCart(Integer memNo) { 
    	
        repository.deleteByMemNo(memNo);
    }
	
    
    @Transactional
    public void deleteCart(Integer cartNo) {
        if (repository.existsById(cartNo)) {
            repository.deleteById(cartNo);
        }
    }
	
	
	public CartDetail convertToCartDetail(CartVO cartVO) {
	    Integer cartNo = cartVO.getCartNo();
	    Integer memNo = cartVO.getMemberVO().getMemNo();
	    Integer prodNo = cartVO.getShopProductVO().getProdNo();
	    String prodName = cartVO.getShopProductVO().getProdName();
	    Integer prodPrice = cartVO.getShopProductVO().getProdPrice();
	    Integer prodQty = cartVO.getProdQty();
	    Integer subtotal = prodPrice * prodQty;

	    return new CartDetail(cartNo, memNo, prodNo, prodName, prodPrice, prodQty, subtotal);
	}
	
	//購物車頁面更新數量用
	public void updateCartQty(Integer cartNo, Integer prodNo, Integer prodQty) {
	    CartVO cartVO = repository.findByCartNo(cartNo);
	    if (cartVO != null) {
	        cartVO.setProdQty(prodQty);
	        repository.save(cartVO);
	    }
	}
	
}
