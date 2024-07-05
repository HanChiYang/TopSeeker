package com.topseeker.artreport.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ArtReportRepository extends JpaRepository<ArtReportVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "delete from art_report where art_report_no =?1", nativeQuery = true)
	void deleteByArtReportNo(int commentNo);

	//● (自訂)條件查詢
	@Query(value = "from ArtReportVO where art_report_no=?1 and art_report_content like?2 order by art_report_no")
	List<ArtReportVO> findByOthers(int artReportNo , int artReportContent);


}
