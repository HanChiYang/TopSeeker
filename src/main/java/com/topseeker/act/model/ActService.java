package com.topseeker.act.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.topseeker.participant.model.ParticipantVO;


@Service("actSerivce")
public class ActService {
	
	@Autowired
	ActRepository repository;

	public void addAct(ActVO actVO) {
		repository.save(actVO);
	}

	public void updateAct(ActVO actVO) {
		repository.save(actVO);
	}

	public void deleteAct(Integer actNo) {
		if (repository.existsById(actNo))
			repository.deleteById(actNo);
	}


	public ActVO getOneAct(Integer actNo) {
		Optional<ActVO> optional = repository.findById(actNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ActVO> getAll() {
		return repository.findAll();
	}

	public Set<ParticipantVO> getParticipantsByActNo(Integer actNo) {
		return getOneAct(actNo).getParticipants();
	}


}
