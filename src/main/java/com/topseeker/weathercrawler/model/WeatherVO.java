package com.topseeker.weathercrawler.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weather")
public class WeatherVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wx_no")
    private Integer wxNo;
    
    @Column(name = "loc_name")
    private String locName;
    @Column(name = "wx_date")
    private Date wxDate;
    @Column(name = "h_temp")
    private String hTemp;
    @Column(name = "m_temp")
    private String mTemp;
    @Column(name = "rain_rate")
    private String rainRate;
    @Column(name = "wx")
    private String wX;
    @Column(name = "uvi_rate")
    private String uviRate;
    @Column(name = "uvi_desc")
    private String uviDesc;
    
    
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
	public String gethTemp() {
		return hTemp;
	}
	public void sethTemp(String hTemp) {
		this.hTemp = hTemp;
	}
	public String getmTemp() {
		return mTemp;
	}
	public void setmTemp(String mTemp) {
		this.mTemp = mTemp;
	}
	public String getRainRate() {
		return rainRate;
	}
	public void setRainRate(String rainRate) {
		this.rainRate = rainRate;
	}
	public String getwX() {
		return wX;
	}
	public void setwX(String wX) {
		this.wX = wX;
	}
	public String getUviRate() {
		return uviRate;
	}
	public void setUviRate(String uviRate) {
		this.uviRate = uviRate;
	}
	public String getUviDesc() {
		return uviDesc;
	}
	public void setUviDesc(String uviDesc) {
		this.uviDesc = uviDesc;
	}
    
}
