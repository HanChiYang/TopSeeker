package com.topseeker.shop.orderdetail.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.product.model.ShopProductVO;



@Entity
@Table(name = "shop_order_detail")
public class OrderDetailVO implements java.io.Serializable {
	private static final long serialVersionUID = 3L;
	
	@EmbeddedId
	private CompositeDetail compositeDetail;
	
	
	@ManyToOne
	@MapsId("orderNo")
	@JoinColumn(name = "order_no", insertable = false, updatable = false)
	private OrderVO orderVO;
	
	
	@ManyToOne
	@MapsId("prodNo")
	@JoinColumn(name = "prod_no", insertable = false, updatable = false)
	private ShopProductVO productVO;
	
	@Column(name = "order_price")
	@NotNull(message = "請輸入購買金額")
	private Integer orderPrice;
	
	@Column(name = "order_qty")
	@NotNull(message = "請輸入購買數量")
	@Min(value = 1, message = "購買數量不能小於{value}")
	private Integer orderQty;
		
	
	  public OrderDetailVO() {
	        this.compositeDetail = new CompositeDetail();
	        this.orderVO = new OrderVO();
	    }
	
	 public CompositeDetail getCompositeDetail() {
		return compositeDetail;
     }

	 public void setCompositeDetail(CompositeDetail compositeDetail) {
        this.compositeDetail = compositeDetail;
	 }
	
	public Integer getOrderPrice() {
		return orderPrice;
	}

	public OrderVO getOrderVO() {
		return orderVO;
	}

	public void setOrderVO(OrderVO orderVO) {
		this.orderVO = orderVO;
	}

	public ShopProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ShopProductVO productVO) {
		this.productVO = productVO;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}
	
	

}
