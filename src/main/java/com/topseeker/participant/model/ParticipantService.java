package com.topseeker.participant.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.act.model.ActRepository;
import com.topseeker.act.model.ActVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Participant;

@Service("participantService")
public class ParticipantService {
	
	
	@Autowired
	ParticipantRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
    private ActRepository actRepo;

	public void addParticipant(ParticipantVO participantVO) {
		ActVO managedActVO = actRepo.findById(participantVO.getActVO().getActNo()).orElseThrow();
        participantVO.setActVO(managedActVO);
		repository.save(participantVO);
	}

	public void updateParticipant(ParticipantVO participantVO) {
		repository.save(participantVO);
	}

	public void deleteParticipant(Integer actPartNo) {
		if (repository.existsById(actPartNo))
			repository.deleteByActPartNo(actPartNo);
//		    repository.deleteById(actPartNo);
	}

	public ParticipantVO getOneParticipant(Integer actPartNo) {
		Optional<ParticipantVO> optional = repository.findById(actPartNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ParticipantVO> getAll() {
		return repository.findAll();
	}

	public List<ParticipantVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Participant.getAllC(map,sessionFactory.openSession());
	}
	
	// 新增方法根據活動編號查找參與者
	public List<ParticipantVO> findParticipantsByActNo(int actNo) {
		return repository.findByActNo(actNo);
	}
}
