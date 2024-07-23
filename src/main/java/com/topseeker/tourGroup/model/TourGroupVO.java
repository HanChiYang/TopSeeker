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


@Entity  
@Table(name = "tour_group") 
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



	public TourGroupVO() { }

	@Id 
	@Column(name = "group_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
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
    @NotNull(message="{typeMismatch.tourGroupVO.groupBegin}")	
    @Future(message="日期必須是在今日(不含)之後")
	public Date getGroupBegin() {
		return groupBegin;
	}

	public void setGroupBegin(Date groupBegin) {
		this.groupBegin = groupBegin;
	}

	@Column(name = "group_end")
    @NotNull(message="{typeMismatch.tourGroupVO.groupEnd}")
    @Future(message="日期必須是在今日(不含)之後")
	public Date getGroupEnd() {
		return groupEnd;
	}

	public void setGroupEnd(Date groupEnd) {
		this.groupEnd = groupEnd;
	}

	@Column(name = "group_deadline")
    @NotNull(message="日期請勿空白")	
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
	public Integer getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(Integer groupStatus) {
		this.groupStatus = groupStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}


