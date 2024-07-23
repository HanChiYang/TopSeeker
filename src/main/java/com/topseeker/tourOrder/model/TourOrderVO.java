package com.topseeker.tourOrder.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.topseeker.member.model.MemberVO;
import com.topseeker.tourGroup.model.TourGroupVO;

@Entity
@Table(name = "tour_order")
public class TourOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no", updatable = false)
    private Integer orderNo;

    @Column(name = "mem_no", insertable = false, updatable = false)
    private Integer memNo;

    @Column(name = "group_no", insertable = false, updatable = false)
    private Integer groupNo;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "departure_date")
    private Date departureDate;

    @Column(name = "order_nums")
    private Integer orderNums;

    @Column(name = "order_price")
    private Integer orderPrice;

    @Column(name = "order_pay")
    private Byte orderPay;

    @Column(name = "order_status")
    private Byte orderStatus;

    @Column(name = "order_star")
    private Byte orderStar;

    @Column(name = "order_comment")
    private String orderComment;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mem_No", referencedColumnName = "mem_No")
	private MemberVO memberVO;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_No", referencedColumnName = "group_No")
	private TourGroupVO tourGroupVO;

    // Getters and Setters
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

//    public Integer getMemNo() {
//        return memNo;
//    }
//
//    public void setMemNo(Integer memNo) {
//        this.memNo = memNo;
//    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(Integer orderNums) {
        this.orderNums = orderNums;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Byte getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(Byte orderPay) {
        this.orderPay = orderPay;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Byte getOrderStar() {
        return orderStar;
    }

    public void setOrderStar(Byte orderStar) {
        this.orderStar = orderStar;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }

    public TourGroupVO getTourGroupVO() {
        return tourGroupVO;
    }

    public void setTourGroupVO(TourGroupVO tourGroupVO) {
        this.tourGroupVO = tourGroupVO;
    }
}
