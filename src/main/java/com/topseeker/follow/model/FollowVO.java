package com.topseeker.follow.model;

 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.topseeker.member.model.MemberVO;
 
@Entity
@Table(name = "follow")
public class FollowVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "follow_no", updatable = false, insertable = false)
	private Integer followNo;
	
	@Column(name = "mem_no")
	private Integer memNo;
	
	@Column(name = "be_followed_mem_no")
	private Integer beFollowedMemNo;

	public Integer getFollowNo() {
		return followNo;
	}

	public void setFollowNo(Integer followNo) {
		this.followNo = followNo;
	}

	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	public Integer getBeFollowedMemNo() {
		return beFollowedMemNo;
	}

	public void setBeFollowedMemNo(Integer beFollowedMemNo) {
		this.beFollowedMemNo = beFollowedMemNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}	
	

