package com.topseeker.knowledge.model;

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
@Table(name = "knowledge")
public class KnowledgeVO implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="know_no", updatable = false, insertable = false)
	private Integer knowNo;
	
	
	@NotEmpty(message="標題: 請勿空白")
	@Pattern(regexp = "^.{2,30}$", message = "標題: 長度必需在2到30之間")
	@Column(name ="know_title")
	private String knowTitle;
	
	@NotEmpty(message="文章內容: 請勿空白")
	@Column(name ="know_content")
	private String knowContent;
	
	@Column(name ="know_pic", columnDefinition = "longblob")
//	@NotNull(message="商品照片: 請上傳照片")
	private byte[] knowPic;
	
	@NotNull(message="發布日期: 請勿空白")
	@Column(name ="know_publish_date", updatable = false)
	private Date knowPublishDate;
	
	@Column(name ="know_status")
	private Integer knowStatus;


	public Integer getKnowNo() {
		return knowNo;
	}

	public void setKnowNo(Integer knowNo) {
		this.knowNo = knowNo;
	}

	public String getKnowTitle() {
		return knowTitle;
	}

	public void setKnowTitle(String knowTitle) {
		this.knowTitle = knowTitle;
	}

	public String getKnowContent() {
		return knowContent;
	}

	public void setKnowContent(String knowContent) {
		this.knowContent = knowContent;
	}

	public byte[] getKnowPic() {
		return knowPic;
	}

	public void setKnowPic(byte[] knowPic) {
		this.knowPic = knowPic;
	}

	public Date getKnowPublishDate() {
		return knowPublishDate;
	}

	public void setKnowPublishDate(Date knowPublishDate) {
		this.knowPublishDate = knowPublishDate;
	}

	public Integer getKnowStatus() {
		return knowStatus;
	}

	public void setKnowStatus(Integer knowStatus) {
		this.knowStatus = knowStatus;
	}
	

}

