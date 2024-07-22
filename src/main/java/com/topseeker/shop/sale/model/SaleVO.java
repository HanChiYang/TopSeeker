package com.topseeker.shop.sale.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.aspectj.lang.annotation.Before;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "shop_sale")
public class SaleVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer saleNo;
	private String saleName;
	private Timestamp saleStdate;
	private Timestamp saleEddate;
	private Integer saleAmount;
	private Double saleDiscount;
	
	public SaleVO() {
		
	}
	
	@Id
	@Column(name = "sale_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(Integer saleNo) {
		this.saleNo = saleNo;
	}
	
	@Column(name = "sale_name")
	@NotEmpty(message="請輸入促銷活動名稱")
	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	
	@Column(name = "sale_stdate")
	@NotNull(message="請輸入起始日期")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Timestamp getSaleStdate() {
		return saleStdate;
	}

	public void setSaleStdate(Timestamp saleStdate) {
		this.saleStdate = saleStdate;
	}
	
	@Column(name = "sale_eddate")
	@NotNull(message="請輸入結束日期")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Timestamp getSaleEddate() {
		return saleEddate;
	}

	public void setSaleEddate(Timestamp saleEddate) {
		this.saleEddate = saleEddate;
	}
	
	@Column(name = "sale_amount")
	@NotNull(message="請正確輸入活動金額門檻")
	public Integer getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(Integer saleAmount) {
		this.saleAmount = saleAmount;
	}
	
	@Column(name = "sale_discount")
	@NotNull(message="請正確輸入促銷折扣")
	@DecimalMin(value = "0.01", message = "方案折扣: 不能小於{value}")
	@DecimalMax(value = "0.99", message = "方案折扣: 不能超過{value}")
	public Double getSaleDiscount() {
		return saleDiscount;
	}

	public void setSaleDiscount(Double saleDiscount) {
		this.saleDiscount = saleDiscount;
	}
	
	//驗證起始日期早於結束日期
	public boolean isStartDateBeforeEndDate(Timestamp saleStdate, Timestamp saleEddate) {
		if (this.saleStdate == null) 
            this.saleStdate = new Timestamp(System.currentTimeMillis());
        
        if (this.saleEddate == null) 
            this.saleEddate = new Timestamp(System.currentTimeMillis());
		
		return this.saleStdate.before(this.saleEddate);
    }
	

}
