package com.topseeker.news.model;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import com.topseeker.newspic.model.NewsPicVO;
/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "news") //代表這個class是對應到資料庫的實體table 
public class NewsVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id  
	@Column(name = "news_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	private Integer newsNo;
	
//	@ManyToOne
//	@JoinColumn(name = "mem_no")
//	private MemberVO memberVO;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="newsVO")
	@OrderBy("newsImgNo asc")
	private Set<NewsPicVO> newsPic = new HashSet<NewsPicVO>();
	
	@Column(name = "news_title")
	@NotEmpty(message="新聞標題: 請勿空白")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$", message = "活動標題: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	private String newsTitle;
	
	@Column(name = "news_content")
	@NotEmpty(message="新聞內容: 請勿空白")
//	@Size(min=1,max=1000,message="活動內容: 長度必需在{min}到{max}之間")
	private String newsContent;	
	
	@Column(name = "news_publish_time")
	@NotNull(message="新聞發布日期: 請勿空白")	
//	@Future(message="日期必須是在今日(不含)之後")
	@Past(message="日期必須是在今日(含)之前")
//	@DateTimeFormat(pattern="yyyy-MM-dd") 
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date newsPublishTime;
	
	public NewsVO() {
	}

//	public Set<NewsPicVO> getNewsPic() {
//	return this.newsPic;
//	}
//
//	public void setNewsPic(Set<NewsPicVO> newsPic) {
//	this.newsPic = newsPic;
//	}
	
	public Integer getNewsNo() {
		return newsNo;
	}

	public void setNewsNo(Integer newsNo) {
		this.newsNo = newsNo;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public Date getNewsPublishTime() {
		return newsPublishTime;
	}

	public void setNewsPublishTime(Date newsPublishTime) {
		this.newsPublishTime = newsPublishTime;
	}	
	
	
}
