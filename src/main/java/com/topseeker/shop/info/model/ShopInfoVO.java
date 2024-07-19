package com.topseeker.shop.info.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "shop_info")
public class ShopInfoVO implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="info_no", updatable = false, insertable = false)
	private Integer infoNo;
	
	@NotEmpty(message="標題: 請勿空白")
	@Pattern(regexp = "^.{2,200}$", message = "標題: 長度必需在2到200之間")
	@Column(name ="info_head")
	private String infoHead;
	
	@NotEmpty(message="文章內容: 請勿空白")
	@Column(name ="info_content")
	private String infoContent;
	
	@Column(name ="info_pic", columnDefinition = "longblob")
//	@NotNull(message="最新消息圖片: 請上傳圖片")
	private byte[] infoPic;
	
	@NotNull(message="發布日期: 請勿空白")
	@Column(name ="info_date", updatable = false)
	private Date infoDate;
	
	@Column(name ="info_status")
	private Integer infoStatus;

	public Integer getInfoNo() {
		return infoNo;
	}

	public void setInfoNo(Integer infoNo) {
		this.infoNo = infoNo;
	}

	public String getInfoHead() {
		return infoHead;
	}

	public void setInfoHead(String infoHead) {
		this.infoHead = infoHead;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public byte[] getInfoPic() {
		return infoPic;
	}

	public void setInfoPic(byte[] infoPic) {
		this.infoPic = infoPic;
	}

	public Date getInfoDate() {
		return infoDate;
	}

	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}

	public Integer getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(Integer infoStatus) {
		this.infoStatus = infoStatus;
	}


}