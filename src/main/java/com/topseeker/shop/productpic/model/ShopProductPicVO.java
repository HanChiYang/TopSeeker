package com.topseeker.shop.productpic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.topseeker.shop.product.model.ShopProductVO;

@Entity
@Table(name = "shop_product_pic")
public class ShopProductPicVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="prod_pic_no", updatable = false, insertable = false)
	private Integer prodPicNo;
	
	@ManyToOne
	@JoinColumn(name = "prod_no", referencedColumnName = "prod_no")
	private ShopProductVO shopProductVO;
	
	@Column(name ="prod_pic", columnDefinition = "longblob")
//	@NotEmpty(message="商品照片: 請上傳照片")
	private byte[] prodPic;
	
	public Integer getProdPicNo() {
		return prodPicNo;
	}
	public void setProdPicNo(Integer prodPicNo) {
		this.prodPicNo = prodPicNo;
	}
	public ShopProductVO getShopProductVO() {
		return shopProductVO;
	}
	public void setShopProductVO(ShopProductVO shopProductVO) {
		this.shopProductVO = shopProductVO;
	}
	public byte[] getProdPic() {
		return prodPic;
	}
	public void setProdPic(byte[] prodPic) {
		this.prodPic = prodPic;
	}
	

}