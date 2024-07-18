package com.topseeker.participant.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.topseeker.act.model.ActVO;
import com.topseeker.member.model.MemberVO;

@Entity // 要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "participant") // 代表這個class是對應到資料庫的實體table，目前對應的table是EMP2
public class ParticipantVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id // @Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵
	@Column(name = "act_part_no") // @Column指這個屬性是對應到資料庫Table的哪一個欄位 //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue的generator屬性指定要用哪個generator
														// //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE,
														// TABLE】
	private Integer actPartNo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "act_no") // 指定用來join table的column
	private ActVO actVO;

	@ManyToOne
	@JoinColumn(name = "mem_no") // 指定用來join table的column
	private MemberVO memberVO;

	@Column(name = "act_join_count")
	@NotNull(message = "參加人數: 請勿空白")
	private Integer actJoinCount;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "act_join_time")
	private Timestamp actJoinTime;

	@Column(name = "act_commit")
	private Integer actCommit;

	@Column(name = "act_star")
	private Integer actStar;

	@Column(name = "act_eva")
	private String actEva;

	public ParticipantVO() { // 必需有一個不傳參數建構子(JavaBean基本知識)

		this.actJoinTime = new Timestamp(System.currentTimeMillis());
		this.actCommit = 0;
		this.actJoinCount = 1;
	}

	public Integer getActPartNo() {
		return actPartNo;
	}

	public void setActPartNo(Integer actPartNo) {
		this.actPartNo = actPartNo;
	}

	public ActVO getActVO() {
		return actVO;
	}

	public void setActVO(ActVO actVO) {
//		   if (actVO != null && actVO.getActNo() != null) {
//	            this.actVO = new ActVO();
//	            this.actVO.setActNo(actVO.getActNo());
//	        }
      this.actVO = actVO;

	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public Integer getActJoinCount() {
		return actJoinCount;
	}

	public void setActJoinCount(Integer actJoinCount) {
		this.actJoinCount = actJoinCount;
	}

	public Timestamp getActJoinTime() {
		return actJoinTime;
	}

	public void setActJoinTime(Timestamp actJoinTime) {
		this.actJoinTime = actJoinTime;
	}

	public Integer getActCommit() {
		return actCommit;
	}

	public void setActCommit(Integer actCommit) {
		this.actCommit = actCommit;
	}

	public Integer getActStar() {
		return actStar;
	}

	public void setActStar(Integer actStar) {
		this.actStar = actStar;
	}

	public String getActEva() {
		return actEva;
	}

	public void setActEva(String actEva) {
		this.actEva = actEva;
	}

}