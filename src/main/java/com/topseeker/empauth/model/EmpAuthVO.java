package com.topseeker.empauth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.topseeker.authority.model.AuthorityVO;
import com.topseeker.employee.model.EmployeeVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity
@Table(name = "emp_auth")
public class EmpAuthVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "emp_auth_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empAuthNo;
	
	@ManyToOne
	@JoinColumn(name = "emp_no", referencedColumnName = "emp_no")
	private EmployeeVO employeeVO;
	
	public EmployeeVO getEmployeeVO() {
		return employeeVO;
	}

	public void setEmployeeVO(EmployeeVO employeeVO) {
		this.employeeVO = employeeVO;
	}
	
	@ManyToOne
	@JoinColumn(name = "auth_no", referencedColumnName = "auth_no")
	private AuthorityVO authorityVO;

	public AuthorityVO getAuthorityVO() {
		return authorityVO;
	}

	public void setAuthorityVO(AuthorityVO authorityVO) {
		this.authorityVO = authorityVO;
	}

//	@Column(name = "emp_no")
//	private Integer empNo;

//	@Column(name = "auth_no")
//	private Integer authNo;
	
	
	public EmpAuthVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}


	public Integer getEmpAuthNo() {
		return empAuthNo;
	}


	public void setEmpAuthNo(Integer empAuthNo) {
		this.empAuthNo = empAuthNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
