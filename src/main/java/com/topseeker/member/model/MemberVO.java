package com.topseeker.member.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "member")
public class MemberVO implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "mem_no", updatable = false, insertable = false)
	private Integer memNo;
	
	@Column (name = "mem_name")
	@NotEmpty(message="姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)]{2,10}$", message = "姓名: 只能是中、英文，且長度必需在2到10之間")
	private String memName;
	
	
	@Column (name = "mem_sex", columnDefinition = "char")
	@NotEmpty(message="性別: 請勿空白")
	private String memSex;
	
	@Column (name = "mem_phone")
	@NotEmpty(message="手機: 請勿空白")
	@Pattern(regexp = "^09\\d{8}$", message = "手機格式不符")
	private String memPhone;
	

	@Column (name = "mem_email")
	@NotEmpty(message="電子信箱: 請勿空白")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "信箱格式不符")
	private String memEmail;
	
	@Column (name = "mem_uid")
	@Pattern(regexp = "^[A-Za-z][1-2]\\d{8}", message = "身分證字號格式不符")
	@NotEmpty(message="身分證字號: 請勿空白")
	private String memUid;
	
	@Column (name = "mem_birthday")
//	@NotNull(message="生日: 請勿空白")	
	private Date memBirthday;
	
	@Column (name = "mem_account")
	@NotEmpty(message="帳號: 請勿空白")
	private String memAccount;
	
	@Column (name = "mem_password")
	@NotEmpty(message="密碼: 請勿空白")
	private String memPassword;
	
	@Column (name = "mem_img", columnDefinition = "longblob")
	private byte[] memImg;
	
	@Column (name = "mem_status")
	private Byte memStatus;

	public MemberVO() {
     
	}
	
	//供忘記密碼使用
	public MemberVO(Integer memNo, String memEmail) {
        this.memNo = memNo;
        this.memEmail = memEmail;
        }
	
	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemSex() {
		return memSex;
	}

	public void setMemSex(String memSex) {
		this.memSex = memSex;
	}

	public String getMemPhone() {
		return memPhone;
	}

	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}

	public String getMemEmail() {
		return memEmail;
	}

	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}

	public String getMemUid() {
		return memUid;
	}

	public void setMemUid(String memUid) {
		this.memUid = memUid;
	}

	public Date getMemBirthday() {
		return memBirthday;
	}

	public void setMemBirthday(Date memBirthday) {
		this.memBirthday = memBirthday;
	}

	public String getMemAccount() {
		return memAccount;
	}

	public void setMemAccount(String memAccount) {
		this.memAccount = memAccount;
	}

	public String getMemPassword() {
		return memPassword;
	}

	public void setMemPassword(String memPassword) {
		this.memPassword = memPassword;
	}

	public byte[] getMemImg() {
		return memImg;
	}

	public void setMemImg(byte[] memImg) {
		this.memImg = memImg;
	}

	public Byte getMemStatus() {
		return memStatus;
	}

	public void setMemStatus(Byte memStatus) {
		this.memStatus = memStatus;
	}

}
