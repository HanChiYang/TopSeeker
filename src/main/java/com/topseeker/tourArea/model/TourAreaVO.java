package com.topseeker.tourArea.model;

import java.util.HashSet;
import java.util.Set;

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

import com.topseeker.tour.model.TourVO;

@Entity
@Table(name = "tour_area")
public class TourAreaVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer areaNo;
	private String areaName;

	private Set<TourVO> tours = new HashSet<TourVO>();

	public TourAreaVO() {
	}

	@Id
	@Column(name = "area_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getAreaNo() {
		return this.areaNo;
	}

	public void setAreaNo(Integer areaNo) {
		this.areaNo = areaNo;
	}

	@Column(name = "area_name")
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="tourAreaVO")
	@OrderBy("tourNo asc")
	//註1:【現在是設定成 cascade="all" lazy="false" inverse="true"之意】
	//註2:【mappedBy="多方的關聯屬性名"：用在雙向關聯中，把關係的控制權反轉】【deptVO是EmpVO的屬性】
	//註3:【原預設為@OneToMany(fetch=FetchType.LAZY, mappedBy="deptVO")之意】--> 【是指原為  lazy="true"  inverse="true"之意】
	//FetchType.EAGER : Defines that data must be eagerly fetched
	//FetchType.LAZY  : Defines that data can be lazily fetched
	public Set<TourVO> getTours() {
		return this.tours;
	}

	public void setTours(Set<TourVO> tours) {
		this.tours = tours;
	}

}
