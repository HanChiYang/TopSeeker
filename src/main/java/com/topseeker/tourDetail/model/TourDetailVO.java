package com.topseeker.tourDetail.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.topseeker.tour.model.TourVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity
@Table(name = "tour_detail")
public class TourDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    
    @Id
    @Column(name = "detail_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
    private Integer detailNo; 
    
//    @Column(name = "tour_no")
//    private Integer tourNo;
    
    @NotNull(message="{typeMismatch.tourGroupVO.groupEnd}")
//    @NotEmpty(message="不可空白")
//    @NotNull(message="不可空白")
    @Column(name = "detail_day")
    private Integer detailDay;

    @Column(name = "detail_con")
    private String detailCon;

    @Column(name = "detail_pic")
    private byte[] detailPic;

    @ManyToOne
    @JoinColumn(name = "tour_no" , nullable = false)
    private TourVO tourVO;
    
    // 動態生成描述的方法
    public String getDetailDayDesc() {
        return "第" + this.getDetailDay() + "天";
    }

    

	    public TourDetailVO() {
		super();
	}
	    


		public TourDetailVO(Integer detailNo, Integer detailDay, String detailCon, byte[] detailPic,
				TourVO tourVO) {
			super();
			this.detailNo = detailNo;
//			this.tourNo = tourNo;
			this.detailDay = detailDay;
			this.detailCon = detailCon;
			this.detailPic = detailPic;
			this.tourVO = tourVO;
		}



		// Getters and setters
		
	    
	
	    public Integer getDetailNo() {
			return detailNo;
		}

		public void setDetailNo(Integer detailNo) {
			this.detailNo = detailNo;
		}


//		public Integer getTourNo() {
//	        return tourNo;
//	    }
//		
//		public void setTourNo(Integer tourNo) {
//	        this.tourNo = tourNo;
//	    }
	
	    public Integer getDetailDay() {
	        return detailDay;
	    }
	
	    public void setDetailDay(Integer detailDay) {
	        this.detailDay = detailDay;
	    }
	
	    public String getDetailCon() {
	        return detailCon;
	    }
	
	    public void setDetailCon(String detailCon) {
	        this.detailCon = detailCon;
	    }
	
	    public byte[] getDetailPic() {
	        return detailPic;
	    }
	
	    public void setDetailPic(byte[] detailPic) {
	        this.detailPic = detailPic;
	    }
	
	    public TourVO getTourVO() {
	        return tourVO;
	    }
	
	    public void setTourVO(TourVO tourVO) {
	        this.tourVO = tourVO;
	    }
	    
////	    ===========================================
//	    public static class TourDetailPK implements Serializable {
//	        private Integer tourNo;
//	        private Integer detailDay;
//	        
//	        
//	        public TourDetailPK() {
//				super();
//			}
//			public TourDetailPK(Integer tourNo, Integer detailDay) {
//				super();
//				this.tourNo = tourNo;
//				this.detailDay = detailDay;
//			}
//
//
//			// Getters, setters, hashCode, equals methods
//	        public Integer getTourNo() {
//	            return tourNo;
//	        }
//
//	        public void setTourNo(Integer tourNo) {
//	            this.tourNo = tourNo;
//	        }
//
//	        public Integer getDetailDay() {
//	            return detailDay;
//	        }
//
//	        public void setDetailDay(Integer detailDay) {
//	            this.detailDay = detailDay;
//	        }
//
//	        @Override
//	        public boolean equals(Object o) {
//	            if (this == o) return true;
//	            if (o == null || getClass() != o.getClass()) return false;
//	            TourDetailPK that = (TourDetailPK) o;
//	            return Objects.equals(tourNo, that.tourNo) && Objects.equals(detailDay, that.detailDay);
//	        }
//
//	        @Override
//	        public int hashCode() {
//	            return Objects.hash(tourNo, detailDay);
//	        }
//	    	}
}