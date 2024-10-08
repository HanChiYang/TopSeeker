// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.notification.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationRepository extends JpaRepository<NotificationVO, Integer> {

	// 修改通知狀態
	@Modifying
	@Query(value = "update NotificationVO n set n.notiStatus = 1 where n.notiStatus = 0 and n.memNo = ?1")
	void readAllNoti(Integer memNo);

	// 取得單一會員通知
	@Query(value = "from NotificationVO where memNo = ?1 order by notiTime desc")
	List<NotificationVO> getMemNoti(Integer memNo);

	//取得最新通知
	@Query(value = "from NotificationVO where memNo = ?1 and notiStatus = 0 order by notiTime desc")
	List<NotificationVO> listNewNoti(Integer memNo);

	// 單一會員通知狀態
	@Query(value = "SELECT DISTINCT * FROM notification WHERE mem_no = ?1 order by noti_time", nativeQuery = true)
	NotificationVO forNotiStatus(Integer memNo);
}