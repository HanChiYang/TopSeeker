package com.topseeker.replymessage.model;
 
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

import com.topseeker.member.model.MemberVO;
import com.topseeker.message.model.MessageVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "reply_message") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ReplyMessageVO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer actMsgRpNo;
	private String actMsgRp;
	private Timestamp actMsgRpTime;
	private MessageVO messageVO;
	private MemberVO memberVO;
	
	public ReplyMessageVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
		
		  this.actMsgRpTime = new Timestamp(System.currentTimeMillis()); 
	}
	
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "act_msg_rp_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getActMsgRpNo() {
		return actMsgRpNo;
	}
	public void setActMsgRpNo(Integer actMsgRpNo) {
		this.actMsgRpNo = actMsgRpNo;
	}
	
	
	@Column(name = "act_msg_rp")
	public String getActMsgRp() {
		return actMsgRp;
	}
	public void setActMsgRp(String actMsgRp) {
		this.actMsgRp = actMsgRp;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "act_msg_rp_time")
	public Timestamp getActMsgRpTime() {
		return actMsgRpTime;
	}
	public void setActMsgRpTime(Timestamp actMsgRpTime) {
		this.actMsgRpTime = actMsgRpTime;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "act_msg_no")   // 指定用來join table的column
	public MessageVO getMessageVO() {
		return messageVO;
	}
	public void setMessageVO(MessageVO messageVO) {
		this.messageVO = messageVO;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	public MemberVO getMemberVO() {
		return memberVO;
	}
	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	

}
