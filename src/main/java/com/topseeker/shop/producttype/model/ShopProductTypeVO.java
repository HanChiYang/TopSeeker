package com.topseeker.shop.producttype.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.topseeker.shop.product.model.ShopProductVO;

@Entity
@Table(name = "shop_product_type")
public class ShopProductTypeVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="prod_type_no", updatable = false, insertable = false)
	private Integer prodTypeNo;
	
	@Column(name ="prod_type_name")
	private String prodTypeName;
	
    @OneToMany(mappedBy = "shopProductTypeVO", cascade = CascadeType.ALL)
    private Set<ShopProductVO> shopProducts = new HashSet<>();
	
	
	public ShopProductTypeVO() {
	}
	
	public Integer getProdTypeNo() {
		return prodTypeNo;
	}
	public void setProdTypeNo(Integer prodTypeNo) {
		this.prodTypeNo = prodTypeNo;
	}
	public String getProdTypeName() {
		return prodTypeName;
	}
	public void setProdTypeName(String prodTypeName) {
		this.prodTypeName = prodTypeName;
	}

	public Set<ShopProductVO> getShopProducts() {
		return this.shopProducts;
	}

	public void setShopProducts(Set<ShopProductVO> shopProducts) {
		this.shopProducts = shopProducts;
	}
	
}
	
