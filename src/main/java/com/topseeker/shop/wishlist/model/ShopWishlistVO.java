package com.topseeker.shop.wishlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.topseeker.shop.wishlist.model.ShopWishlistVO.CompositeWishlist;

@Entity
@Table(name = "shop_wishlist")
@IdClass(CompositeWishlist.class)
public class ShopWishlistVO implements java.io.Serializable {

	@Id
	@Column(name = "mem_no", updatable = false, insertable = false)
	private Integer memNo;

	@Id
	@Column(name = "prod_no", updatable = false, insertable = false)
	private Integer prodNo;

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

	// 特別加上對複合主鍵物件的 getter / setter
	public CompositeWishlist getCompositeWishlistKey() {
		return new CompositeWishlist(memNo, prodNo);
	}

	public void setCompositeWishlistKey(CompositeWishlist key) {
		this.memNo = key.getMemNo();
		this.prodNo = key.getProdNo();
	}

	// 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
	static class CompositeWishlist implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer memNo;
		private Integer prodNo;
		
		// 一定要有無參數建構子
		public CompositeWishlist() {
			super();
		}

		public CompositeWishlist(Integer memNo, Integer prodNo) {
			super();
			this.memNo = memNo;
			this.prodNo = prodNo;
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
		
		// 一定要 override 此類別的 hashCode() 與 equals() 方法！
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
			result = prime * result + ((prodNo == null) ? 0 : prodNo.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (obj != null && getClass() == obj.getClass()) {
				CompositeWishlist compositeWishlistKey = (CompositeWishlist) obj;
				if (memNo.equals(compositeWishlistKey.memNo) && prodNo.equals(compositeWishlistKey.prodNo)) {
					return true;
				}
			}

			return false;
		}

	}
	

}