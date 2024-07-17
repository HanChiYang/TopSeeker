// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tour.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TourRepository extends JpaRepository<TourVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from tour where tour_no =?1", nativeQuery = true)
	void deleteByTourNo(int tourNo);

	//● (自訂)條件查詢
	@Query(value = "from TourVO where tourNo=?1 and tourName like?2 and tourDays=?3 order by tourNo")
	List<TourVO> findByOthers(int tourNo , String tourName , int tourDays);
}