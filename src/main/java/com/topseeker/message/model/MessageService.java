package com.topseeker.message.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Message;


@Service("messageService")
public class MessageService {
	
	
	@Autowired
	MessageRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addMessage(MessageVO messageVO) {
		repository.save(messageVO);
	}

	public void updateMessage(MessageVO messageVO) {
		repository.save(messageVO);
	}

	public void deleteMessage(Integer actMsgNo) {
		if (repository.existsById(actMsgNo))
			repository.deleteByActMsgNo(actMsgNo);
//		    repository.deleteById(actPartNo);
	}

	public MessageVO getOneMessage(Integer actMsgNo) {
		Optional<MessageVO> optional = repository.findById(actMsgNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<MessageVO> getAll() {
		return repository.findAll();
	}

	public List<MessageVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Message.getAllC(map,sessionFactory.openSession());
	}
	

}
