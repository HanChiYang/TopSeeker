package com.topseeker.replymessage.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.replymessage.model.ReplyMessageRepository;
import com.topseeker.replymessage.model.ReplyMessageVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Message;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_ReplyMessage;
 
@Service("replymessageService")
public class ReplyMessageService {
	
	@Autowired
	ReplyMessageRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addReplyMessage(ReplyMessageVO replymessageVO) {
		repository.save(replymessageVO);
	}

	public void updateReplyMessage(ReplyMessageVO replymessageVO) {
		repository.save(replymessageVO);
	}

	public void deleteReplyMessage(Integer actMsgRpNo) {
		if (repository.existsById(actMsgRpNo))
			repository.deleteByActMsgNo(actMsgRpNo);
//		    repository.deleteById(actPartNo);
	}

	public ReplyMessageVO getOneReplyMessage(Integer actMsgRpNo) {
		Optional<ReplyMessageVO> optional = repository.findById(actMsgRpNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ReplyMessageVO> getAll() {
		return repository.findAll();
	}

	public List<ReplyMessageVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ReplyMessage.getAllC(map,sessionFactory.openSession());
	}

}
