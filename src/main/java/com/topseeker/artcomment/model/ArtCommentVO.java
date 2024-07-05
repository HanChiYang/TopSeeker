package com.topseeker.artcomment.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.topseeker.article.model.ArticleVO;
import com.topseeker.member.model.MemberVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "art_comment") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ArtCommentVO {
	
	private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "comment_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer commentNo;
	
	@ManyToOne
	@JoinColumn(name = "art_no")   // 指定用來join table的column
	private ArticleVO articleVO;
	
	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	private MemberVO memberVO;
	
	@Column(name = "comment_content")
	private String commentContent;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "comment_post_time", updatable = false)
	private Timestamp commentPostTime;
	
	
	public ArtCommentVO() {
		  this.commentPostTime = new Timestamp(System.currentTimeMillis()); 
		

	}

	public Integer getCommentNo() {
		return commentNo;
	}

	public void setCommentNo(Integer commentNo) {
		this.commentNo = commentNo;
	}

	public ArticleVO getArticleVO() {
		return articleVO;
	}

	public void setArticleVO(ArticleVO articleVO) {
		this.articleVO = articleVO;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Timestamp getCommentPostTime() {
		return commentPostTime;
	}

	public void setCommentPostTime(Timestamp commentPostTime) {
		this.commentPostTime = commentPostTime;
	}
	
	
	

}
