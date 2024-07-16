package com.topseeker.article.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;


import com.topseeker.artpic.model.ArtPicVO;
import com.topseeker.artcomment.model.ArtCommentVO;
import com.topseeker.member.model.MemberVO;

@DynamicUpdate
@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "article") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ArticleVO {

	private static final long serialVersionUID = 1L;
	
	
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "art_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer artNo;
	
	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	private MemberVO memberVO;
	
	@Column(name = "art_title")
	private String artTitle;
	
	@Column(name = "art_content")
	private String artContent;
	
	@Column(name = "art_status")
	private Integer artStatus;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "art_publish_time", updatable = false)
	private Timestamp artPublishTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "art_update_time" , insertable = false)
	private Timestamp artUpdateTime;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="articleVO")
	@OrderBy("artPic asc")
	private List<ArtPicVO> artPics = new ArrayList<ArtPicVO>();
	
	 //---------------------------------------------新增的地方
	 
	 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "articleVO")
	 @OrderBy("commentNo asc") 
	 private Set<ArtCommentVO> artcomments = new HashSet<ArtCommentVO>();
	 
	 

	 public Set<ArtCommentVO> getArtcomments() {
	  return artcomments;
	 }

	 public void setArtcomments(Set<ArtCommentVO> artcomments) {
	  this.artcomments = artcomments;
	 }
	// -------------------------------------留言

	public List<ArtPicVO> getArtPics() {
		return artPics;
	}

	public void setArtPics(List<ArtPicVO> artPics) {
		this.artPics = artPics;
	}

	public ArticleVO() {
		  this.artPublishTime = new Timestamp(System.currentTimeMillis()); 
		  this.artUpdateTime = new Timestamp(System.currentTimeMillis()); 
		  this.artStatus = 1;

	}
	
	public Integer getArtNo() {
		return artNo;
	}

	public void setArtNo(Integer artNo) {
		this.artNo = artNo;
	}
	

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	public String getArtTitle() {
		return artTitle;
	}

	public void setArtTitle(String artTitle) {
		this.artTitle = artTitle;
	}
	
	public String getArtContent() {
		return artContent;
	}

	public void setArtContent(String artContent) {
		this.artContent = artContent;
	}
	
	public Integer getArtStatus() {
		return artStatus;
	}

	public void setArtStatus(Integer artStatus) {
		this.artStatus = artStatus;
	}
	

	public Timestamp getArtPublishTime() {
		return artPublishTime;
	}

	public void setArtPublishTime(Timestamp artPublishTime) {
		this.artPublishTime = artPublishTime;
	}
	

	public Timestamp getArtUpdateTime() {
		return artUpdateTime;
	}

	public void setArtUpdateTime(Timestamp artUpdateTime) {
		this.artUpdateTime = artUpdateTime;
	}
	
	
	
	
	
	
	
	
	

}
