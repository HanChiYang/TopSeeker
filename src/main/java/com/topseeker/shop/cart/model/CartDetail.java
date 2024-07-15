package com.topseeker.shop.cart.model;

import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.sale.model.SaleVO;

public class CartDetail {
	
	private Integer cartNo;
	private Integer memNo;
    private Integer prodNo;
    private String prodName;
    private Integer prodPrice;
    private Integer prodQty;
    private Integer subtotal;
    
    
    
    private String subtotalJson;
    private OrderVO orderVO;
    private SaleVO saleVO;
    
    
    public CartDetail() {
		super();
	}
    


	public CartDetail(Integer cartNo, Integer memNo, Integer prodNo, String prodName, Integer prodPrice,
			Integer prodQty, Integer subtotal) {
		super();
		this.cartNo = cartNo;
		this.memNo = memNo;
		this.prodNo = prodNo;
		this.prodName = prodName;
		this.prodPrice = prodPrice;
		this.prodQty = prodQty;
		this.subtotal = subtotal;
	}


	public Integer getCartNo() {
    	return cartNo;
    }
    public void setCartNo(Integer cartNo) {
    	this.cartNo = cartNo;
    }
    
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public Integer getProdNo() {
		return prodNo;
	}
	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Integer getProdPrice() {
		return prodPrice;
	}
	public void setProdPrice(Integer prodPrice) {
		this.prodPrice = prodPrice;
	}
	public Integer getProdQty() {
		return prodQty;
	}
	public void setProdQty(Integer prodQty) {
		this.prodQty = prodQty;
	}
	public Integer getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Integer subtotal) {
		this.subtotal = subtotal;
	}
	@Override
	public String toString() {
		return "CartDetail [cartNo=" + cartNo + ", memNo=" + memNo + ", prodNo=" + prodNo + ", prodName=" + prodName
				+ ", prodPrice=" + prodPrice + ", prodQty=" + prodQty + ", subtotal=" + subtotal + "]";
	}



	public String getSubtotalJson() {
		return subtotalJson;
	}



	public void setSubtotalJson(String subtotalJson) {
		this.subtotalJson = subtotalJson;
	}



	public OrderVO getOrderVO() {
		return orderVO;
	}



	public void setOrderVO(OrderVO orderVO) {
		this.orderVO = orderVO;
	}



	public SaleVO getSaleVO() {
		return saleVO;
	}



	public void setSaleVO(SaleVO saleVO) {
		this.saleVO = saleVO;
	}
	
	
	
    
}
