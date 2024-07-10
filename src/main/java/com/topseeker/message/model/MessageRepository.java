package com.topseeker.message.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface MessageRepository extends JpaRepository<MessageVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from message where act_msg_no =?1", nativeQuery = true)
	void deleteByActMsgNo(int actMsgNo);

	//● (自訂)條件查詢
	@Query(value = "from MessageVO where act_msg_no=?1 and act_msg like?2 order by act_msg_no")
	List<MessageVO> findByOthers(int actMsgNo , int actMsg);

}
