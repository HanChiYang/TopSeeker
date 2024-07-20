package com.topseeker.shop.complaints.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ComplaintVO{
		
	  private Integer orderNo;
	  private String name;
	  
	  @NotEmpty(message="請輸入希望收到回覆的電子信箱")
	  private String email;
	  
	  @NotEmpty(message="請輸入您對此筆訂單的想法")
	  @Size(max = 300, message = "字數限制為300字，請精簡內容")
	  private String message;
	
	  
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	  
	  
	  
	  
}
