package com.topseeker.employee.model;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

//import com.topseeker.report.model.ReportVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "employee") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class EmployeeVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer empNo;
	private String empName;
//	private Set<ReportVO> reports = new HashSet<ReportVO>();
	
	public EmployeeVO() {
		
	}

	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "emp_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 	
	public Integer getEmpNo() {
		return empNo;
	}

	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="memberVO")
//	@OrderBy("act_rp_no asc")
//	public Set<ReportVO> getReports() {
//		return this.reports;
//	}
//
//
//	public void setReports(Set<ReportVO> reports) {
//		this.reports = reports;
//	}

	
	@Column(name = "emp_name")
	public String getEmpName() {
		return empName;
	}


	public void setEmpName(String empName) {
		this.empName = empName;
	}


	

}
