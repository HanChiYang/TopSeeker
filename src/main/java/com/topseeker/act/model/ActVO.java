package com.topseeker.act.model;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.OrderBy;

import com.topseeker.participant.model.ParticipantVO;



@Entity
@Table(name = "act")
public class ActVO implements java.io.Serializable {
	
private static final long serialVersionUID = 1L;
	
	private Integer actNo;
	private String actTitle;
	private Set<ParticipantVO> participants = new HashSet<ParticipantVO>();
	
	public ActVO() {
	}
	
	
	@Id
	@Column(name = "act_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	
	public Integer getActNo() {
		return actNo;
	}

	public void setActNo(Integer actNo) {
		this.actNo = actNo;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="actVO")
	@OrderBy("act_part_no asc")	
	public Set<ParticipantVO> getParticipants() {
		return this.participants;
	}

	public void setParticipants(Set<ParticipantVO> participants) {
		this.participants = participants;
	}

	@Column(name = "act_title")
	public String getActTitle() {
		return actTitle;
	}


	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}
	
	

}
