package com.topseeker.artreport.model;

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
import com.topseeker.employee.model.EmployeeVO;
import com.topseeker.member.model.MemberVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "art_report") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ArtReportVO {
	
	private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "art_report_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer artReportNo;
	
	@ManyToOne
	@JoinColumn(name = "art_no")   // 指定用來join table的column
	private ArticleVO articleVO;
	
	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	private MemberVO memberVO;
	
	@ManyToOne
	@JoinColumn(name = "emp_no")   // 指定用來join table的column
	private EmployeeVO employeeVO;
	
	@Column(name = "art_report_content")
	private String artReportContent;
	
	@Column(name = "art_report_status")
	private Integer artReportStatus;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "art_report_date", updatable = false)
	private Timestamp artReportDate;
	
	
	public ArtReportVO() {
		  this.artReportDate = new Timestamp(System.currentTimeMillis()); 
		

	}


	public Integer getArtReportNo() {
		return artReportNo;
	}


	public void setArtReportNo(Integer artReportNo) {
		this.artReportNo = artReportNo;
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


	public EmployeeVO getEmployeeVO() {
		return employeeVO;
	}


	public void setEmployeeVO(EmployeeVO employeeVO) {
		this.employeeVO = employeeVO;
	}


	public String getArtReportContent() {
		return artReportContent;
	}


	public void setArtReportContent(String artReportContent) {
		this.artReportContent = artReportContent;
	}


	public Integer getArtReportStatus() {
		return artReportStatus;
	}


	public void setArtReportStatus(Integer artReportStatus) {
		this.artReportStatus = artReportStatus;
	}


	public Timestamp getArtReportDate() {
		return artReportDate;
	}


	public void setArtReportDate(Timestamp artReportDate) {
		this.artReportDate = artReportDate;
	}
	
	
	

}
