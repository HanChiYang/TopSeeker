package com.topseeker.tourGroup.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.topseeker.tour.model.TourVO;
import com.topseeker.tourArea.model.TourAreaVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "tour_group") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class TourGroupVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Integer groupNo;
	private TourVO tourVO;
	private Integer groupPrice;
	private Date groupBegin;
	private Date groupEnd;
	private Date groupDeadline;
	private Integer groupBal;
	private Integer groupMin;
	private Integer groupMax;
	private Integer groupStatus;
	

	public TourGroupVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}
	
	
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "group_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	public Integer getGroupNo() {
		return groupNo;
	}




	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}



	@ManyToOne
	@JoinColumn(name = "tour_no")
	public TourVO getTourVO() {
		return tourVO;
	}




	public void setTourVO(TourVO tourVO) {
		this.tourVO = tourVO;
	}



	@Column(name = "group_price")
	@NotNull(message="行程價格: 請勿空白")
	public Integer getGroupPrice() {
		return groupPrice;
	}




	public void setGroupPrice(Integer groupPrice) {
		this.groupPrice = groupPrice;
	}



	@Column(name = "group_begin")
    @NotNull(message="行程開始日期: 請勿空白")	
    @Future(message="日期必須是在今日(不含)之後")
////@Past(message="日期必須是在今日(含)之前")
////@DateTimeFormat(pattern="yyyy-MM-dd") 
////@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	public Date getGroupBegin() {
		return groupBegin;
	}




	public void setGroupBegin(Date groupBegin) {
		this.groupBegin = groupBegin;
	}



	@Column(name = "group_end")
    @NotNull(message="行程結束日期: 請勿空白")	
    @Future(message="日期必須是在今日(不含)之後")
	public Date getGroupEnd() {
		return groupEnd;
	}



	
	public void setGroupEnd(Date groupEnd) {
		this.groupEnd = groupEnd;
	}



	@Column(name = "group_deadline")
    @NotNull(message="報名截止日期: 請勿空白")	
    @Future(message="日期必須是在今日(不含)之後")
	public Date getGroupDeadline() {
		return groupDeadline;
	}




	public void setGroupDeadline(Date groupDeadline) {
		this.groupDeadline = groupDeadline;
	}



	@Column(name = "group_bal")
	@NotNull(message="剩餘名額: 請勿空白")
	@Min(value = 1, message = "名額數最小為{value}")
	public Integer getGroupBal() {
		return groupBal;
	}




	public void setGroupBal(Integer groupBal) {
		this.groupBal = groupBal;
	}



	@Column(name = "group_min")
	@NotNull(message="成行人數: 請勿空白")
	@Min(value = 1, message = "人數最小為{value}")
	public Integer getGroupMin() {
		return groupMin;
	}




	public void setGroupMin(Integer groupMin) {
		this.groupMin = groupMin;
	}



	@Column(name = "group_max")
	@NotNull(message="最大人數: 請勿空白")
	@Min(value = 1, message = "人數最小為{value}")
	public Integer getGroupMax() {
		return groupMax;
	}



	
	public void setGroupMax(Integer groupMax) {
		this.groupMax = groupMax;
	}



	@Column(name = "group_status")
	@NotNull(message="開團狀態: 請勿空白")
//	@Min(value = 1, message = "狀態只能輸入(1上架 2下架 3取消 4延期 )")
//	@Min(value = 4, message = "狀態只能輸入(1上架 2下架 3取消 4延期 )")
	public Integer getGroupStatus() {
		return groupStatus;
	}




	public void setGroupStatus(Integer groupStatus) {
		this.groupStatus = groupStatus;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}




//	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
//	@Column(name = "tour_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
//	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
//	public Integer getTourNo() {
//		return tourNo;
//	}
//
//
//	public void setTourNo(Integer tourNo) {
//		this.tourNo = tourNo;
//	}

	
//	@Column(name = "tour_name")
//	@NotEmpty(message="行程名稱: 請勿空白")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,10}$", message = "行程姓名: 只能是中、英文字母、數字和_ , 且長度必需在1到10之間")
//	public String getTourName() {
//		return tourName;
//	}
//
//
//	public void setTourName(String tourName) {
//		this.tourName = tourName;
//	}
//
//	@Column(name = "tour_days")
//	@NotNull(message="行程天數: 請勿空白")
//	@Min(value = 1, message = "天數最小為{value}")
//	@Max(value = 999, message = "天數最大為{value}")
//	public Integer getTourDays() {
//		return tourDays;
//	}
//
//
//	public void setTourDays(Integer tourDays) {
//		this.tourDays = tourDays;
//	}
//
//	@Column(name = "tour_pic")
//	public byte[] getTourPic() {
//		return tourPic;
//	}
//
//	
//
//	public void setTourPic(byte[] tourPic) {
//		this.tourPic = tourPic;
//	}
//
//	@Column(name = "tour_guys")
//	@NotNull(message="行程人數: 請勿空白")
//	@Min(value = 0, message = "人數最小為{value}")
//	public Integer getTourGuys() {
//		return tourGuys;
//	}
//
//
//	public void setTourGuys(Integer tourGuys) {
//		this.tourGuys = tourGuys;
//	}
//
//	@Column(name = "tour_star")
//	@NotNull(message="行程星數: 請勿空白")
//	@Min(value = 0, message = "星數最小為{value}")
//	public Integer getTourStar() {
//		return tourStar;
//	}
//
//
//	public void setTourStar(Integer tourStar) {
//		this.tourStar = tourStar;
//	}
//
//	@Column(name = "tour_status")
//	@NotEmpty(message="行程狀態: 請勿空白")
//	@Pattern(regexp = "^[(TF)]$", message = "行程狀態: 只能是T或F，T為上架，F為下架")
//	public String getTourStatus() {
//		return tourStatus;
//	}
//
//
//	public void setTourStatus(String tourStatus) {
//		this.tourStatus = tourStatus;
//	}
//
//	// @ManyToOne  (雙向多對一/一對多) 的多對一
//	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
//	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
//	@ManyToOne
//	@JoinColumn(name = "area_no")   // 指定用來join table的column
//	public TourAreaVO getTourAreaVO() {
//		return tourAreaVO;
//	}
//
//
//	public void setTourAreaVO(TourAreaVO tourAreaVO) {
//		this.tourAreaVO = tourAreaVO;
//	}


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
