package com.topseeker.weathercrawler.model;

import java.sql.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "weather")
public class WeatherVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "wx_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer wxNo;
	
	@Column(name = "loc_name")
	private String locName;
	
	@Column(name = "wx_date")
	private Date wxDate;
	
	@Column(name = "wxh_temp")
	private Integer wxhTemp;
	
	@Column(name = "wxm_temp")
	private Integer wxmTemp;
	
	public WeatherVO() {
		
	}

	public Integer getWxNo() {
		return wxNo;
	}

	public void setWxNo(Integer wxNo) {
		this.wxNo = wxNo;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	public Date getWxDate() {
		return wxDate;
	}

	public void setWxDate(Date wxDate) {
		this.wxDate = wxDate;
	}

	public Integer getWxhTemp() {
		return wxhTemp;
	}

	public void setWxhTemp(Integer wxhTemp) {
		this.wxhTemp = wxhTemp;
	}

	public Integer getWxmTemp() {
		return wxmTemp;
	}

	public void setWxmTemp(Integer wxmTemp) {
		this.wxmTemp = wxmTemp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	
}
