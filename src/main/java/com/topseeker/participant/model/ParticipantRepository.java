package com.topseeker.participant.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ParticipantRepository extends JpaRepository<ParticipantVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from participant where act_part_no =?1", nativeQuery = true)
	void deleteByActPartNo(int actPartNo);

	//● (自訂)條件查詢
	@Query(value = "from ParticipantVO where act_part_no=?1 and act_commit like?2 order by act_part_no")
	List<ParticipantVO> findByOthers(int actPartNo , int actCommit);
	
	// 新增方法根據活動編號查找參與者
	@Query("from ParticipantVO where actVO.actNo = ?1")
	List<ParticipantVO> findByActNo(int actNo);
}
