package com.topseeker.authority.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.topseeker.empauth.model.EmpAuthVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "authority") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class AuthorityVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "authorityVO", cascade = CascadeType.ALL)
	private Set<EmpAuthVO> empAuthVO;
	
	public Set<EmpAuthVO> getEmpAuthVO() {
		return empAuthVO;
	}

	public void setEmpAuthVO(Set<EmpAuthVO> empAuthVO) {
		this.empAuthVO = empAuthVO;
	}
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	@Column(name = "auth_no")
	private Integer authNo;
	
	@Column(name = "auth_name")
	private String authName;
	
	@Column(name = "auth_info")
	private String authInfo;
	

	public AuthorityVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	public Integer getAuthNo() {
		return this.authNo;
	}

	public void setAuthNo(Integer authNo) {
		this.authNo = authNo;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authInfo, authName, authNo, empAuthVO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorityVO other = (AuthorityVO) obj;
		return Objects.equals(authInfo, other.authInfo) && Objects.equals(authName, other.authName)
				&& Objects.equals(authNo, other.authNo) && Objects.equals(empAuthVO, other.empAuthVO);
	}


	
}

