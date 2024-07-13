package com.topseeker.artpic.model;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.topseeker.article.model.ArticleVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "art_pic") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ArtPicVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "art_pic_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer artPicNo;
	
	@Column(name = "art_pic")
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
	private byte[] artPic;
	
	@ManyToOne
	@JoinColumn(name = "art_no")   // 指定用來join table的column
	private ArticleVO articleVO;
	
	
	public ArtPicVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}


	public Integer getArtPicNo() {
		return artPicNo;
	}


	public void setArtPicNo(Integer artPicNo) {
		this.artPicNo = artPicNo;
	}


	public byte[] getArtPic() {
		return artPic;
	}


	public void setArtPic(byte[] artPic) {
		this.artPic = artPic;
	}


	public ArticleVO getArticleVO() {
		return articleVO;
	}


	public void setArticleVO(ArticleVO articleVO) {
		this.articleVO = articleVO;
	}
	
	public String getBase64ArtPic() {
        return Base64.getEncoder().encodeToString(this.artPic);
    }


	public void setArticleVO(Integer artNo) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
