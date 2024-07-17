// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tourDetail.model;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.topseeker.tour.model.TourVO;
import com.topseeker.tourGroup.model.TourGroupVO;

@Repository
public interface TourDetailRepository extends JpaRepository<TourDetailVO, Integer> {
    
	@Transactional
	@Modifying
	@Query(value = "delete from tour_detail where detail_no =?1", nativeQuery = true)
	void deleteByDetailNo(int detailNo);
//
////	//● (自訂)條件查詢
//	@Query(value ="SELECT tourNo from TourDetailVO Group By tourNo=?1", nativeQuery = true)
//	List<TourDetailVO> selectTourNo(Map map);

	@Query(value = "from TourDetailVO where tourNo=?1   order by tourNo")
	List<TourDetailVO> findByOthers(int tourNo  );
	
	TourDetailVO findByTourVOAndDetailDay(TourVO tourVO, Integer detailDay);
	TourDetailVO findByDetailNo(Integer detailNo);
	List<TourDetailVO> findAllByTourVO(TourVO tourVO);
    List<TourDetailVO> findAllByTourVOAndDetailDay(TourVO tourVO, Integer detailDay);
//    List<TourDetailVO> findAllByTourNoAndDetailDay(Integer tourNo, Integer detailDay);
	  
    List<TourDetailVO> findByTourVO_TourNo(Integer tourNo);

}

