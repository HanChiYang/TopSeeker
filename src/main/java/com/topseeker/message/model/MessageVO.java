package com.topseeker.message.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
import com.topseeker.employee.model.EmployeeVO;
import com.topseeker.member.model.MemberVO;


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "message") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class MessageVO implements java.io.Serializable {
	
private static final long serialVersionUID = 1L;
	
	private Integer actMsgNo;
	private String actMsg;
	private MemberVO memberVO;
	private ActVO actVO;	
    private	Timestamp actMsgTime;



	public MessageVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
		
		  this.actMsgTime = new Timestamp(System.currentTimeMillis()); 
	}


	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "act_msg_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getActMsgNo() {
		return actMsgNo;
	}




	public void setActMsgNo(Integer actMsgNo) {
		this.actMsgNo = actMsgNo;
	}
	
	@Column(name = "act_msg")
	public String getActMsg() {
		return actMsg;
	}



	public void setActMsg(String actMsg) {
		this.actMsg = actMsg;
	}



	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	public MemberVO getMemberVO() {
		return memberVO;
	}




	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}



	@ManyToOne
	@JoinColumn(name = "act_no")   // 指定用來join table的column
	public ActVO getActVO() {
		return actVO;
	}




	public void setActVO(ActVO actVO) {
		this.actVO = actVO;
	}



	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "act_msg_time")
	public Timestamp getActMsgTime() {
		return actMsgTime;
	}



	public void setActMsgTime(Timestamp actMsgTime) {
		this.actMsgTime = actMsgTime;
	}





	
	

	


}
