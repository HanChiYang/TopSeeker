package com.topseeker.shop.wishlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.product.model.ShopProductVO;

@Entity
@Table(name = "shop_wishlist")
//@IdClass(CompositeWishlist.class)
public class ShopWishlistVO implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="wishlist_no", updatable = false, insertable = false)
	private Integer wishlistNo;
	
	@ManyToOne
	@JoinColumn(name = "mem_no", referencedColumnName = "mem_no")
	private MemberVO memberVO;

	@ManyToOne
	@JoinColumn(name = "prod_no", referencedColumnName = "prod_no")
	private ShopProductVO shopProductVO;

	
	public Integer getWishlistNo() {
		return wishlistNo;
	}

	public void setWishlistNo(Integer wishlistNo) {
		this.wishlistNo = wishlistNo;
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

    //============架構變更，故不使用複合主鍵============
	
//	// 特別加上對複合主鍵物件的 getter / setter
//	public CompositeWishlist getCompositeWishlistKey() {
//		return new CompositeWishlist(memNo, prodNo);
//	}
//
//	public void setCompositeWishlistKey(CompositeWishlist key) {
//		this.memNo = key.getMemNo();
//		this.prodNo = key.getProdNo();
//	}
//
//	// 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
//	static class CompositeWishlist implements Serializable {
//		private static final long serialVersionUID = 1L;
//
//		private Integer memNo;
//		private Integer prodNo;
//		
//		// 一定要有無參數建構子
//		public CompositeWishlist() {
//			super();
//		}
//
//		public CompositeWishlist(Integer memNo, Integer prodNo) {
//			super();
//			this.memNo = memNo;
//			this.prodNo = prodNo;
//		}
//
//		public Integer getMemNo() {
//			return memNo;
//		}
//		public void setMemNo(Integer memNo) {
//			this.memNo = memNo;
//		}
//		public Integer getProdNo() {
//			return prodNo;
//		}
//		public void setProdNo(Integer prodNo) {
//			this.prodNo = prodNo;
//		}
//		
//		// 一定要 override 此類別的 hashCode() 與 equals() 方法！
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
//			result = prime * result + ((prodNo == null) ? 0 : prodNo.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//
//			if (obj != null && getClass() == obj.getClass()) {
//				CompositeWishlist compositeWishlistKey = (CompositeWishlist) obj;
//				if (memNo.equals(compositeWishlistKey.memNo) && prodNo.equals(compositeWishlistKey.prodNo)) {
//					return true;
//				}
//			}
//
//			return false;
//		}
//
//	}
}