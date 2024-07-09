package com.topseeker.shop.order.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;
import com.topseeker.shop.sale.model.SaleVO;


@Entity
@Table(name = "shop_order")
public class OrderVO implements java.io.Serializable {
	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_no")
	private Integer orderNo;
	
	@ManyToOne
	@JoinColumn(name = "mem_no")
	private MemberVO memberVO;
	
	@ManyToOne
	@JoinColumn(name = "sale_no")
	private SaleVO saleVO;
	
	
	@Column(name = "order_date")
	@NotNull(message="請輸入訂購日期")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp orderDate;
	
	@Column(name = "order_amount")
	@NotNull(message="訂單金額請勿空白")
	private Integer orderAmount;
	
	@Column(name = "order_shipment")
	@NotNull(message="運費金額請勿空白")
	private Integer orderShipment;
	
	@Column(name = "order_discount")
	@NotNull(message="折扣金額請勿空白")
	private Integer orderDiscount;
	
	@Column(name = "order_bill")
	@NotNull(message="請款金額請勿空白")
	private Integer orderBill;
	
	@Column(name = "order_name")
	@NotEmpty(message="請輸入訂購人姓名")
	private String orderName;
	
	@Column(name = "order_phone")
	@NotEmpty(message="請輸入連絡電話")
	private String orderPhone;
	
	@Column(name = "order_address")
	@NotEmpty(message="請輸入收件地址")
	private String orderAddress;
	
	@Column(name = "order_status")
	private Integer orderStatus;
	
	@Column(name = "payment_status")
	private Integer paymentStatus;
	
	@Column(name = "payment_method")
	private Integer paymentMethod;
	
	@Column(name = "delivery_method")
	private Integer deliveryMethod;
	
	@Column(name = "remarks")	
	private String remarks;
	
	public OrderVO() {
	}
	

	@OneToMany(mappedBy = "orderVO", cascade = CascadeType.ALL)
	private List<OrderDetailVO> orderDetails = new ArrayList<>();
	
	
	public List<OrderDetailVO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailVO> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public void addOrderDetail(OrderDetailVO orderDetailVO) {
		 orderDetailVO.setOrderVO(this);
	     this.orderDetails.add(orderDetailVO);
	}
	

	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	public SaleVO getSaleVO() {
		return this.saleVO;
	}

	public void setSaleVO(SaleVO saleVO) {
		this.saleVO = saleVO;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getOrderShipment() {
		return orderShipment;
	}

	public void setOrderShipment(Integer orderShipment) {
		this.orderShipment = orderShipment;
	}

	public Integer getOrderDiscount() {
		return orderDiscount;
	}

	public void setOrderDiscount(Integer orderDiscount) {
		this.orderDiscount = orderDiscount;
	}

	public Integer getOrderBill() {
		return orderBill;
	}

	public void setOrderBill(Integer orderBill) {
		this.orderBill = orderBill;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(Integer deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	

}
