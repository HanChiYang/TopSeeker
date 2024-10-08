package com.topseeker.report.model;
 
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
@Table(name = "report") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ReportVO implements java.io.Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "act_rp_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer actRpNo;
	
	
	@ManyToOne
	@JoinColumn(name = "mem_no")   // 指定用來join table的column
	private MemberVO memberVO;
	
	@ManyToOne
	@JoinColumn(name = "act_no")   // 指定用來join table的column
	private ActVO actVO;	
	
	@Column(name = "act_report")
	private Integer actReport;
	
	@Column(name = "act_report_text")
	private String actReportText;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "act_report_time" , updatable = false)
	private	Timestamp actReportTime;
	
	
	@ManyToOne
	@JoinColumn(name = "emp_no" )   // 指定用來join table的column	
	private EmployeeVO employeeVO;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "handle_time" , insertable = false)
	private Timestamp handleTime;
	
	@Column(name = "handle_check")
	private Integer handleCheck;
	
	@Column(name = "handle_reply")
	private String handleReply;	

	public ReportVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
		
		this.actReportTime = new Timestamp(System.currentTimeMillis()); 
		this.handleTime = new Timestamp(System.currentTimeMillis()); 
		  this.handleCheck = 0;

	}


	
	public Integer getActRpNo() {
		return actRpNo;
	}




	public void setActRpNo(Integer actRpNo) {
		this.actRpNo = actRpNo;
	}



	public MemberVO getMemberVO() {
		return memberVO;
	}




	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}



	public ActVO getActVO() {
		return actVO;
	}




	public void setActVO(ActVO actVO) {
		this.actVO = actVO;
	}



	public Integer getActReport() {
		return actReport;
	}




	public void setActReport(Integer actReport) {
		this.actReport = actReport;
	}



	public String getActReportText() {
		return actReportText;
	}




	public void setActReportText(String actReportText) {
		this.actReportText = actReportText;
	}



	public Timestamp getActReportTime() {
		return actReportTime;
	}




	public void setActReportTime(Timestamp actReportTime) {
		this.actReportTime = actReportTime;
	}



	public EmployeeVO getEmployeeVO() {
		return employeeVO;
	}



	public void setEmployeeVO(EmployeeVO employeeVO) {
		this.employeeVO = employeeVO;
	}




	public Timestamp getHandleTime() {
		return handleTime;
	}




	public void setHandleTime(Timestamp handleTime) {
		this.handleTime = handleTime;
	}



	public Integer getHandleCheck() {
		return handleCheck;
	}




	public void setHandleCheck(Integer handleCheck) {
		this.handleCheck = handleCheck;
	}



	public String getHandleReply() {
		return handleReply;
	}




	public void setHandleReply(String handleReply) {
		this.handleReply = handleReply;
	}




	
	

	


}
