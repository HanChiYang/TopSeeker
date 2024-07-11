package com.topseeker.notification.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.topseeker.notification.model.NotificationRepository;

@Service("notificationService")
public class NotificationService {

	@Autowired
	NotificationRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 新增通知
	public void addNoti(NotificationVO notificationVO) {
		repository.save(notificationVO);
	}

	// 取得所有單一會員的通知
	public List<NotificationVO> getMemNoti(Integer memNo) {
		return repository.getMemNoti(memNo);
	}

	//取得所有未讀通知
	public List<NotificationVO> listNewNoti(Integer memNo) {
		return repository.listNewNoti(memNo);
	}
	
	// 修改通知狀態
	public void readAllNoti(Integer memNo) {
		repository.readAllNoti(memNo);
	}
	
	// 單一會員通知狀態
	public NotificationVO forNotiStatus(Integer memNo) {
		return repository.forNotiStatus(memNo);
	}

}
