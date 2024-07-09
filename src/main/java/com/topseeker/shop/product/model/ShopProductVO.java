package com.topseeker.shop.product.model;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//import com.topseeker.shop.orderdetail.model.OrderDetailVO;
import com.topseeker.shop.productpic.model.ShopProductPicVO;
import com.topseeker.shop.producttype.model.ShopProductTypeVO;


@Entity
@Table(name = "shop_product")
public class ShopProductVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="prod_no", updatable = false, insertable = false)
	private Integer prodNo;
	
	
	@ManyToOne
	@JoinColumn(name = "prod_type_no", referencedColumnName = "prod_type_no")
	private ShopProductTypeVO shopProductTypeVO;
	
	
	@Column(name ="prod_name")
	@NotEmpty(message="商品名稱: 請勿空白")
	@Size(min=1,max=100,message="商品名稱: 長度必需在{min}到{max}之間")
	private String prodName;
	
	
	@Column(name ="prod_info")
	@NotEmpty(message="商品介紹: 請勿空白")
	@Size(min=2,max=1000,message="商品介紹: 長度必需在{min}到{max}之間")
	private String prodInfo;
	
	
	@Column(name ="prod_price")
	@Min(value = 0, message = "商品定價: 不能小於{value}元")
	private Integer prodPrice;
	
	@Column(name ="prod_status")
	private Integer prodStatus;
	
	@Column(name ="prod_date")
	private Timestamp prodDate;
	

	//商品圖片用
    @OneToMany(mappedBy = "shopProductVO", cascade = CascadeType.ALL)
    private List<ShopProductPicVO> shopProductPics = new ArrayList<>();
    
//	//訂單明細用
//    @OneToMany(mappedBy = "shopProductVO", cascade = CascadeType.ALL)
//    private Set<OrderDetailVO> orderDetails = new HashSet<>();
	
	
	//無參數建構子
	public ShopProductVO() {
	}
	
	public Integer getProdNo() {
		return prodNo;
	}
	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}

	public ShopProductTypeVO getShopProductTypeVO() {
		return shopProductTypeVO;
	}

	public void setShopProductTypeVO(ShopProductTypeVO shopProductTypeVO) {
		this.shopProductTypeVO = shopProductTypeVO;
	}
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(String prodInfo) {
		this.prodInfo = prodInfo;
	}
	public Integer getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(Integer prodPrice) {
		this.prodPrice = prodPrice;
	}
	public Integer getProdStatus() {
		return prodStatus;
	}
	public void setProdStatus(Integer prodStatus) {
		this.prodStatus = prodStatus;
	}
	public Timestamp getProdDate() {
		return prodDate;
	}
	public void setProdDate(Timestamp prodDate) {
		this.prodDate = prodDate;
	}

	public List<ShopProductPicVO> getShopProductPics() {
		return shopProductPics;
	}

	public void setShopProductPics(List<ShopProductPicVO> shopProductPics) {
		this.shopProductPics = shopProductPics;
	}

//	public Set<OrderDetailVO> getOrderDetails() {
//		return orderDetails;
//	}
//
//	public void setOrderDetails(Set<OrderDetailVO> orderDetails) {
//		this.orderDetails = orderDetails;
//	}
	
	
	

}
