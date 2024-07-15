package com.topseeker.act.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.topseeker.act.controller.validation.MemGroup;
import com.topseeker.actpicture.model.ActPictureVO;
import com.topseeker.message.model.MessageVO;
import com.topseeker.member.model.MemberVO;
/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "act") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ActVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id  
	@Column(name = "act_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	private Integer actNo;
	
	@ManyToOne
	@JoinColumn(name = "mem_no")
	private MemberVO memberVO;

//	@Column(name = "mem_no")
//	private Integer memNo;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="actVO")
	@OrderBy("actPicNo asc")
	private List<ActPictureVO> actPictures = new ArrayList<>();
	
	@Column(name = "act_title")
	@NotEmpty(message="活動標題: 請勿空白")
	@Pattern(regexp = "^.{2,10}$", message = "活動標題: 長度必需在2到10之間")
	private String actTitle;
	
	@Column(name = "act_content")
	@NotEmpty(message="活動內容: 請勿空白")
	@Size(min=1,max=800,message="活動內容: 長度必需在{min}到{max}之間")
	private String actContent;
	
	@Column(name = "act_max_count")
	@NotNull(message="可參與最多人數: 請勿空白")
	@Min(value = 2, message = "可參與最少人數: 必須至少為2")
	private Integer actMaxCount;
	
	@Column(name = "act_min_count")
	@NotNull(message="可參與最少人數: 請勿空白")
	@Min(value = 1, message = "可參與最少人數: 必須至少為1")
	private Integer actMinCount;
	
	@Column(name = "act_current_count")
	private Integer actCurrentCount=0;
	
	@Column(name = "act_check_count")
	private Integer actCheckCount=0;
	
	@Column(name = "act_enroll_begin")
	@NotNull(message="報名開始日: 請勿空白")	
//	@FutureOrPresent(message = "日期必須是今天（含）之後") 
	private Date actEnrollBegin;
	
	@Column(name = "act_enroll_end")
	@NotNull(message="報名結束日: 請勿空白")	
	@Future(message="日期必須是在今日(不含)之後", groups = {MemGroup.class})
	private Date actEnrollEnd;
	
	@Column(name = "act_start")	
	@NotNull(message="活動開始日: 請勿空白")
	@Future(message="日期必須是在今日(不含)之後", groups = {MemGroup.class})
	private Date actStart;
	
	@Column(name = "act_end")
	@NotNull(message="活動結束日: 請勿空白")	
	@Future(message="日期必須是在今日(不含)之後", groups = {MemGroup.class})
	private Date actEnd;
	
	@Column(name = "act_place")
	@NotEmpty(message="活動地點: 請勿空白")	
	private String actPlace;
	
	@Column(name = "act_status")	
	private Integer actStatus;
	
	@Column(name = "act_rate_sum")
	private Integer actRateSum=0;
	
	@Column(name = "eval_sum")
	private Integer evalSum=0;
	
	//==============活動留言===================
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "actVO")
    @OrderBy("actMsgNo asc") 
    private Set<MessageVO> messages = new HashSet<MessageVO>();

	public Set<MessageVO> getMessages() {
		return messages;
	}
	
	public void setArtcomments(Set<MessageVO> messages) {
		this.messages = messages;
	}
	//========================================
	public ActVO() {
	}

	public Integer getActNo() {
		return actNo;
	}

	public void setActNo(Integer actNo) {
		this.actNo = actNo;
	}

	public MemberVO getMemberVO() {
		return this.memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public List<ActPictureVO> getActPictures() {
		return this.actPictures;
	}

//	public Integer getMemNo() {
//		return memNo;
//	}
//
//	public void setMemNo(Integer memNo) {
//		this.memNo = memNo;
//	}

	public void setActPictures(List<ActPictureVO> actpictures) {
		this.actPictures = actpictures;
	}
	
	public String getActTitle() {
		return actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}

	public String getActContent() {
		return actContent;
	}

	public void setActContent(String actContent) {
		this.actContent = actContent;
	}

	public Integer getActMaxCount() {
		return actMaxCount;
	}

	public void setActMaxCount(Integer actMaxCount) {
		this.actMaxCount = actMaxCount;
	}

	public Integer getActMinCount() {
		return actMinCount;
	}

	public void setActMinCount(Integer actMinCount) {
		this.actMinCount = actMinCount;
	}

	public Integer getActCurrentCount() {
		return actCurrentCount;
	}

	public void setActCurrentCount(Integer actCurrentCount) {
		this.actCurrentCount = actCurrentCount;
	}

	public Integer getActCheckCount() {
		return actCheckCount;
	}

	public void setActCheckCount(Integer actCheckCount) {
		this.actCheckCount = actCheckCount;
	}

	public Date getActEnrollBegin() {
		return this.actEnrollBegin;
	}

	public void setActEnrollBegin(Date actEnrollBegin) {
		this.actEnrollBegin = actEnrollBegin;
	}

	public Date getActEnrollEnd() {
		return actEnrollEnd;
	}

	public void setActEnrollEnd(Date actEnrollEnd) {
		this.actEnrollEnd = actEnrollEnd;
	}

	public Date getActStart() {
		return this.actStart;
	}

	public void setActStart(Date actStart) {
		this.actStart = actStart;
	}

	public Date getActEnd() {
		return actEnd;
	}

	public void setActEnd(Date actEnd) {
		this.actEnd = actEnd;
	}

	public String getActPlace() {
		return actPlace;
	}

	public void setActPlace(String actPlace) {
		this.actPlace = actPlace;
	}

	public Integer getActStatus() {
		return actStatus;
	}

	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}

	public Integer getActRateSum() {
		return actRateSum;
	}

	public void setActRateSum(Integer actRateSum) {
		this.actRateSum = actRateSum;
	}

	public Integer getEvalSum() {
		return evalSum;
	}

	public void setEvalSum(Integer evalSum) {
		this.evalSum = evalSum;
	}
	
}
