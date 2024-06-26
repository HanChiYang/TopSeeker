package com.topseeker.notification.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class NotificationVO implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "noti_no", updatable = false, insertable = false)
	private Integer notiNo;
	
	@Column (name = "mem_no")
	private Integer memNo;
	@Column (name = "noti_content")
	private String notiContent;
	@Column (name = "noti_time")
	private Timestamp notiTime;
	@Column (name = "noti_status")
	private Byte notiStatus;
	
	public Integer getNotiNo() {
		return notiNo;
	}
	public void setNotiNo(Integer notiNo) {
		this.notiNo = notiNo;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public String getNotiContent() {
		return notiContent;
	}
	public void setNotiContent(String notiContent) {
		this.notiContent = notiContent;
	}
	public Timestamp getNotiTime() {
		return notiTime;
	}
	public void setNotiTime(Timestamp notiTime) {
		this.notiTime = notiTime;
	}
	public Byte getNotiStatus() {
		return notiStatus;
	}
	public void setNotiStatus(Byte notiStatus) {
		this.notiStatus = notiStatus;
	}
}
	
