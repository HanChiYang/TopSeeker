// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tourGroup.model;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TourGroupRepository extends JpaRepository<TourGroupVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from tour_group where group_no =?1", nativeQuery = true)
	void deleteByGroupNo(int groupNo);

//	● (自訂)條件查詢
	@Query(value = "from TourVO where tourNo=?1 and groupPrice =?2  and groupStatus=?3 order by groupNo")
	List<TourGroupVO> findByOthers(int tourNo , int groupPrice , int groupStatus);
	
	
	  List<TourGroupVO> findByTourVO_TourNo(Integer tourNo);
}