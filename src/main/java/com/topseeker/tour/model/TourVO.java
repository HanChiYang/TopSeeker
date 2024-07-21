package com.topseeker.tour.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourArea.model.TourAreaVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "tour") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class TourVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Integer tourNo;
	private String tourName;
	private String tourIntro;
	private Integer tourPrice;
	private Integer tourDays;
	private byte[] tourPic;
	private Integer tourGuys;
	private Integer tourStar;
	private String tourStatus;
	private TourAreaVO tourAreaVO;
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private Set<TourGroupVO> tourGroups = new HashSet<TourGroupVO>();

	public TourVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "tour_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	public Integer getTourNo() {
		return tourNo;
	}


	public void setTourNo(Integer tourNo) {
		this.tourNo = tourNo;
	}

	
	@Column(name = "tour_name")
	@NotEmpty(message="行程名稱: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,10}$", message = "行程名稱: 只能是中、英文字母、數字和_ , 且長度必需在1到10之間")
	public String getTourName() {
		return tourName;
	}


	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
	
	@Column(name = "tour_intro")
	@NotEmpty(message="行程簡介: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,500}$", message = "行程簡介: 只能是中、英文字母、數字和_ , 且長度必需在1到10之間")
	public String getTourIntro() {
		return tourIntro;
	}

	public void setTourIntro(String tourIntro) {
		this.tourIntro = tourIntro;
	}
	
	@Column(name = "tour_price")
	@NotNull(message="行程價格: 請勿空白")
	@Min(value = 1, message = "價格最小為{value}")
	public Integer getTourPrice() {
		return tourPrice;
	}
	
	public void setTourPrice(Integer tourPrice) {
		this.tourPrice = tourPrice;
	}
	
	
	@Column(name = "tour_days")
	@NotNull(message="行程天數: 請勿空白")
	@Min(value = 1, message = "天數最小為{value}")
	@Max(value = 999, message = "天數最大為{value}")
	public Integer getTourDays() {
		return tourDays;
	}


	public void setTourDays(Integer tourDays) {
		this.tourDays = tourDays;
	}

	@Column(name = "tour_pic")
	public byte[] getTourPic() {
		return tourPic;
	}

	

	public void setTourPic(byte[] tourPic) {
		this.tourPic = tourPic;
	}

	@Column(name = "tour_guys")
	@NotNull(message="行程人數: 請勿空白")
	@Min(value = 0, message = "人數最小為{value}")
	public Integer getTourGuys() {
		return tourGuys;
	}


	public void setTourGuys(Integer tourGuys) {
		this.tourGuys = tourGuys;
	}

	@Column(name = "tour_star")
	@NotNull(message="行程星數: 請勿空白")
	@Min(value = 0, message = "星數最小為{value}")
	public Integer getTourStar() {
		return tourStar;
	}


	public void setTourStar(Integer tourStar) {
		this.tourStar = tourStar;
	}

	@Column(name = "tour_status")
	@NotEmpty(message="行程狀態: 請勿空白")
	@Pattern(regexp = "^[(TF)]$", message = "行程狀態: 只能是T或F，T為上架，F為下架")
	public String getTourStatus() {
		return tourStatus;
	}


	public void setTourStatus(String tourStatus) {
		this.tourStatus = tourStatus;
	}

	// @ManyToOne  (雙向多對一/一對多) 的多對一
	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
	@ManyToOne
	@JoinColumn(name = "area_no")   // 指定用來join table的column
	public TourAreaVO getTourAreaVO() {
		return tourAreaVO;
	}


	public void setTourAreaVO(TourAreaVO tourAreaVO) {
		this.tourAreaVO = tourAreaVO;
	}

	
	
	
	
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="tourVO")
	@OrderBy("Group_no asc")
	//註1:【現在是設定成 cascade="all" lazy="false" inverse="true"之意】
	//註2:【mappedBy="多方的關聯屬性名"：用在雙向關聯中，把關係的控制權反轉】【deptVO是EmpVO的屬性】
	//註3:【原預設為@OneToMany(fetch=FetchType.LAZY, mappedBy="deptVO")之意】--> 【是指原為  lazy="true"  inverse="true"之意】
	//FetchType.EAGER : Defines that data must be eagerly fetched
	//FetchType.LAZY  : Defines that data can be lazily fetched
	public Set<TourGroupVO> getTourGroups() {
		return this.tourGroups;
	}

	public void setTourGroups(Set<TourGroupVO> tourGroups) {
		this.tourGroups = tourGroups;
	}
	
	
	
	
	
	
	
	
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//
//
//	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
//	@Column(name = "EMPNO")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
//	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
//	public Integer getEmpno() {
//		return this.empno;
//	}
//
//	public void setEmpno(Integer empno) {
//		this.empno = empno;
//	}
//
//	// @ManyToOne  (雙向多對一/一對多) 的多對一
//	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
//	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
//	@ManyToOne
//	@JoinColumn(name = "DEPTNO")   // 指定用來join table的column
//	public DeptVO getDeptVO() {
//		return this.deptVO;
//	}
//
//	public void setDeptVO(DeptVO deptVO) {
//		this.deptVO = deptVO;
//	}
//
//	@Column(name = "ENAME")
//	@NotEmpty(message="行程姓名: 請勿空白")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "行程姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
//	public String getEname() {
//		return this.ename;
//	}
//
//	public void setEname(String ename) {
//		this.ename = ename;
//	}
//
//	@Column(name = "JOB")
//	@NotEmpty(message="行程職位: 請勿空白")
//	@Size(min=2,max=10,message="行程職位: 長度必需在{min}到{max}之間")
//	public String getJob() {
//		return this.job;
//	}
//
//	public void setJob(String job) {
//		this.job = job;
//	}
//
//	@Column(name = "HIREDATE")
////	@NotNull(message="雇用日期: 請勿空白")	
////	@Future(message="日期必須是在今日(不含)之後")
////	@Past(message="日期必須是在今日(含)之前")
////	@DateTimeFormat(pattern="yyyy-MM-dd") 
////	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
//	public Date getHiredate() {
//		return this.hiredate;
//	}
//
//	public void setHiredate(Date hiredate) {
//		this.hiredate = hiredate;
//	}
//
//	@Column(name = "SAL")
//	@NotNull(message="行程薪水: 請勿空白")
//	@DecimalMin(value = "10000.00", message = "行程薪水: 不能小於{value}")
//	@DecimalMax(value = "99999.99", message = "行程薪水: 不能超過{value}")
//	public Double getSal() {
//		return this.sal;
//	}
//
//	public void setSal(Double sal) {
//		this.sal = sal;
//	}
//
//	@Column(name = "COMM")
//	@NotNull(message="行程獎金: 請勿空白")
//	@DecimalMin(value = "1.00", message = "行程獎金: 不能小於{value}")
//	@DecimalMax(value = "99999.99", message = "行程獎金: 不能超過{value}")
//	public Double getComm() {
//		return this.comm;
//	}
//
//	public void setComm(Double comm) {
//		this.comm = comm;
//	}
//	
//	@Column(name = "UPFILES")
////	@NotEmpty(message="行程照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
//	public byte[] getUpFiles() {
//		return upFiles;
//	}
//	public void setUpFiles(byte[] upFiles) {
//		this.upFiles = upFiles;
//	}
	
}
