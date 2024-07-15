package com.topseeker.employee.model;

import java.sql.Date;
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

@Entity
@Table(name = "employee")
public class EmployeeVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "employeeVO", cascade = CascadeType.ALL)
	private Set<EmpAuthVO> empAuthVO;
	
	public Set<EmpAuthVO> getEmpAuthVO() {
		return empAuthVO;
	}

	public void setEmpAuthVO(Set<EmpAuthVO> empAuthVO) {
		this.empAuthVO = empAuthVO;
	}

	@Id 
	@Column(name = "emp_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empNo;
	
	@Column(name = "emp_name")
	private String empName;

	@Column(name = "emp_account")
	private String empAccount;

	@Column(name = "emp_password")
	private String empPassword;

	@Column(name = "emp_hiredate")
	private Date empHiredate;

	@Column(name = "emp_status")
	private Byte empStatus;

	public EmployeeVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	public Integer getEmpNo() {
		return empNo;
	}

	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpAccount() {
		return empAccount;
	}

	public void setEmpAccount(String empAccount) {
		this.empAccount = empAccount;
	}

	public String getEmpPassword() {
		return empPassword;
	}

	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}

	public Date getEmpHiredate() {
		return empHiredate;
	}

	public void setEmpHiredate(Date empHiredate) {
		this.empHiredate = empHiredate;
	}

	public Byte getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(Byte empStatus) {
		this.empStatus = empStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(empAccount, empAuthVO, empHiredate, empName, empNo, empPassword, empStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeVO other = (EmployeeVO) obj;
		return Objects.equals(empAccount, other.empAccount) && Objects.equals(empAuthVO, other.empAuthVO)
				&& Objects.equals(empHiredate, other.empHiredate) && Objects.equals(empName, other.empName)
				&& Objects.equals(empNo, other.empNo) && Objects.equals(empPassword, other.empPassword)
				&& Objects.equals(empStatus, other.empStatus);
	}



	
	
}
