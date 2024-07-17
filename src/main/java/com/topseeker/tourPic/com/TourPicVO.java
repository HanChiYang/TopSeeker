package com.topseeker.tourPic.com;

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
@Table(name = "tour_pic") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class TourPicVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "pic_no")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer picNo;
	
	@Column(name = "pic_pic")
	private byte[] picPic;

	public Integer getPicNo() {
		return picNo;
	}

	public void setPicNo(Integer picNo) {
		this.picNo = picNo;
	}

	public byte[] getPicPic() {
		return picPic;
	}

	public void setPicPic(byte[] picPic) {
		this.picPic = picPic;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
