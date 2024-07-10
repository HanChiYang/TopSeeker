package com.topseeker.replymessage.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.replymessage.model.ReplyMessageVO;

public interface ReplyMessageRepository extends JpaRepository<ReplyMessageVO, Integer> {

	 
	@Transactional
	@Modifying
	@Query(value = "delete from reply_message where act_msg_rp_no =?1", nativeQuery = true)
	void deleteByActMsgNo(int actMsgNo);

	//● (自訂)條件查詢
	@Query(value = "from ReplyMessageVO where act_msg_rp_no=?1 and act_msg_rp like?2 order by act_msg_rp_no")
	List<ReplyMessageVO> findByOthers(int actMsgRpNo , int actMsgRp);

}
