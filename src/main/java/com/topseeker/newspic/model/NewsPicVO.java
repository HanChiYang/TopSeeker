package com.topseeker.newspic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.topseeker.news.model.NewsVO;

@Entity
@Table(name = "news_pic")
public class NewsPicVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id  
	@Column(name = "news_img_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer newsImgNo;
	
	@ManyToOne
	@JoinColumn(name = "news_no")
	private NewsVO newsVO;
	
	@Column(name = "news_img")
	private byte[] newsImg;

	public Integer getNewsImgNo() {
		return newsImgNo;
	}

	public void setNewsImgNo(Integer newsImgNo) {
		this.newsImgNo = newsImgNo;
	}

	public NewsVO getNewsVO() {
		return newsVO;
	}

	public void setNewsVO(NewsVO newsVO) {
		this.newsVO = newsVO;
	}

	public byte[] getNewsImg() {
		return newsImg;
	}

	public void setNewsImg(byte[] newsImg) {
		this.newsImg = newsImg;
	}	
	
}
