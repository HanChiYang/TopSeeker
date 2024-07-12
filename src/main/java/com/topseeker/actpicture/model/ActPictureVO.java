package com.topseeker.actpicture.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.topseeker.act.model.ActVO;

@Entity
@Table(name = "act_picture")
public class ActPictureVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	@Id  
	@Column(name = "act_pic_no")  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer actPicNo;
	
	@ManyToOne
	@JoinColumn(name = "act_no")
	private ActVO actVO;
	
	@Column(name = "act_pic_name")
	private String actPicName;
	
	@Column(name = "act_pic")
	private byte[] actPic;
	
	public Integer getActPicNo() {
		return actPicNo;
	}
	public void setActPicNo(Integer actPicNo) {
		this.actPicNo = actPicNo;
	}		
	public ActVO getActVO() {
		return this.actVO;
	}
	public void setActVO(ActVO actVO) {
		this.actVO = actVO;
	}
	public String getActPicName() {
		return actPicName;
	}
	public void setActPicName(String actPicName) {
		this.actPicName = actPicName;
	}
	public byte[] getActPic() {
		return actPic;
	}
	public void setActPic(byte[] actPic) {
		this.actPic = actPic;
	}
	
	
	
	
}
