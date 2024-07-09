package com.topseeker.shop.orderdetail.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Embeddable
public class CompositeDetail implements Serializable{
	// 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
				private static final long serialVersionUID = 4L;

				@NotEmpty(message="請輸入訂單編號")
			    @Digits(fraction = 0, integer = 2, message = "訂單編號有誤")
				private Integer orderNo;
			   
				@NotEmpty(message="請輸入商品編號")
				@Digits(fraction = 0, integer = 2, message = "商品編號有誤")
				private Integer prodNo;
				
				// 一定要有無參數建構子
				public CompositeDetail() {
					super();
				}
				

				public CompositeDetail(Integer orderNo, Integer prodNo) {
					super();
					this.orderNo = orderNo;
					this.prodNo = prodNo;
				}

				public Integer getOrderNo() {
					return orderNo;
				}

				public void setOrderNo(Integer orderNo) {
					this.orderNo = orderNo;
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
					result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
					result = prime * result + ((prodNo == null) ? 0 : prodNo.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;

					if (obj != null && getClass() == obj.getClass()) {
						CompositeDetail compositeKey = (CompositeDetail) obj;
						if (orderNo.equals(compositeKey.orderNo) && prodNo.equals(compositeKey.prodNo)) {
							return true;
						}
					}

					return false;
				}
			
}
