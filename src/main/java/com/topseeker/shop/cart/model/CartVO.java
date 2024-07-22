package com.topseeker.shop.cart.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;

import com.google.gson.annotations.Expose;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.product.model.ShopProductVO;

@Entity
@Table(name = "shop_cart")
public class CartVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_no", updatable = false)
	private Integer cartNo;

	@ManyToOne
	@JoinColumn(name = "mem_no", nullable = false)
	private MemberVO memberVO;

	@ManyToOne
	@JoinColumn(name = "prod_no", nullable = false)
	private ShopProductVO shopProductVO;

	@DecimalMin(value = "1", message = "商品數量最少需為${value}件")
	@Column(name = "prod_Qty")
	private Integer prodQty;

	@Transient
	private Integer prodNo; // 接收JSON中的prodNo
	

	@Transient
	private List<CartDetail> cartDetail;

	public CartVO() {
		super();
	}

	public Integer getCartNo() {
		return cartNo;
	}

	public void setCartNo(Integer cartNo) {
		this.cartNo = cartNo;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public ShopProductVO getShopProductVO() {
		return shopProductVO;
	}

	public void setShopProductVO(ShopProductVO shopProductVO) {
		this.shopProductVO = shopProductVO;
	}

	public Integer getProdQty() {
		return prodQty;
	}

	public void setProdQty(Integer prodQty) {
		this.prodQty = prodQty;
	}

	public Integer getProdNo() {
		return prodNo;
	}

	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}

	
	public List<CartDetail> getCartDetail() {
		return cartDetail;
	}

	public void setCartDetail(List<CartDetail> cartDetail) {
		this.cartDetail = cartDetail;
	}

	public CartVO(Integer cartNo, MemberVO memberVO, ShopProductVO shopProductVO, Integer prodQty,
			List<CartDetail> cartDetail) {
		super();
		this.cartNo = cartNo;
		this.memberVO = memberVO;
		this.shopProductVO = shopProductVO;
		this.prodQty = prodQty;
		this.cartDetail = cartDetail;
	}

	@Override
	public String toString() {
		return "CartVO [cartNo=" + cartNo + ", memberVO=" + memberVO + ", shopProductVO=" + shopProductVO + ", prodQty="
				+ prodQty + ", cartDetail=" + cartDetail + "]";
	}

}
