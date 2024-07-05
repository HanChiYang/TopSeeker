package com.topseeker.employee.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

//import com.topseeker.report.model.ReportVO;

public interface EmployeeRepository extends JpaRepository<EmployeeVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from report where act_rp_no =?1", nativeQuery = true)
	void deleteByActPartNo(int actPartNo);

	//● (自訂)條件查詢
//	@Query(value = "from ReportVO where act_rp_no=?1 and act_report like?2 order by act_rp_no")
//	List<ReportVO> findByOthers(int actRpNo , int actReport);

}
