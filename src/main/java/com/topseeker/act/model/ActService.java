package com.topseeker.act.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.participant.model.ParticipantRepository;
import com.topseeker.participant.model.ParticipantService;
import com.topseeker.participant.model.ParticipantVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;

@Transactional
@Service("actService")
public class ActService {

	@Autowired
	ActRepository repository;
	
	@Autowired
	ParticipantRepository participantRepository;
	
	@Autowired
    private ParticipantService participantService;
	
	@Autowired
    private SessionFactory sessionFactory;
	

	
	//新增
	public void addAct(ActVO actVO) {
		repository.save(actVO);
	}
	//修改
	public void updateAct(ActVO actVO) {
		repository.save(actVO);
	}
	//刪除
	public void deleteAct(Integer actNo) {
		if (repository.existsById(actNo))
			repository.deleteByActNo(actNo);

	}

	public ActVO getOneAct(Integer actNo) {
		Optional<ActVO> optional = repository.findById(actNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}
	
	public List<ActVO> getAll() {
		return repository.findAll();
	}

	//取會員的開團活動(未使用)
	public List<ActVO> getActsByMem(Integer memNo) {
		return repository.findActByMem(memNo);
	}

	public List<ActVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Act.getAllC(map,sessionFactory.openSession());
	}
	

	public List<ActVO> findMyOpenGroup(Integer memNoA) {
		return repository.findMyOpenGroup(memNoA);
	}
	//檢舉後修改活動狀態
	public void updateActStatus(Integer actNo, Integer newStatus) {
		  repository.updateActStatus(actNo, newStatus);
	}
	// 從ParticipantRepository取狀態為審核通過的總數，如果為 null 則返回 0
//	public int getTotalJoinCountByActNoAndCommit(Integer actNo) {
//        Integer count = participantRepository.findTotalJoinCountByActNoAndCommit(actNo);
//        return count != null ? count : 0;
//    }
	// 從ParticipantRepository取狀態為待審核的總數，如果為 null 則返回 0
//    public int getPendingJoinCountByActNo(Integer actNo) {
//        Integer count = participantRepository.findPendingJoinCountByActNo(actNo);
//        return count != null ? count : 0;
//    }
    //更新 actCurrentCount、actCheckCount，並在獲取活動列表時調用這個方法
//    @Transactional
//    public void updateActCurrentAndCheckCount(Integer actNo) {
//        int currentCount = getTotalJoinCountByActNoAndCommit(actNo);
//        int pendingCount = getPendingJoinCountByActNo(actNo);
//
//        repository.updateActCurrentCount(actNo, currentCount);
//        repository.updateActCheckCount(actNo, pendingCount);
//    }
	
	
	
	

   
}