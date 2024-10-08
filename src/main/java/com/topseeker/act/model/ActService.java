package com.topseeker.act.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	    ActVO act = repository.findById(actNo)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + actNo));
	    
	    act.setActStatus(3);
	    repository.save(act);
	}

	public ActVO getOneAct(Integer actNo) {
		Optional<ActVO> optional = repository.findById(actNo);
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}
	
	public List<ActVO> getAll() {
		return repository.findAll();
	}

	//取會員的開團活動
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
	//更新待審核人數
	public void updateActCheckCount(Integer actNo, Integer additionalCount) {
        ActVO actVO = getOneAct(actNo);
        if (actVO != null) {
            int newCheckCount = actVO.getActCheckCount() + additionalCount;
            repository.updateActCheckCount(actNo, newCheckCount);
        }
    }
	
	// 定時任務，每天午夜檢查活動狀態
    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜執行一次
//    @Scheduled(cron = "*/30 * * * * *") // 每30秒執行一次
    public void updateActStatusScheduled() {
        Date today = Date.valueOf(LocalDate.now());
        List<ActVO> acts = repository.findAll();
        
        for (ActVO act : acts) {
            // 報名截止日後
            if (act.getActEnrollEnd().before(today)) {
                if (act.getActCurrentCount() >= act.getActMinCount()) {
                    // 若已參與人數達人數下限且狀態為0，狀態改為1(已成團)
                    if (act.getActStatus() == 0) {
                        repository.updateActStatus(act.getActNo(), 1);
                    }
                } else {
                    // 若已參與人數未達人數下限且狀態為0，狀態改為3(已取消)
                    if (act.getActStatus() == 0) {
                        repository.updateActStatus(act.getActNo(), 3);
                    }
                }
            }
            
            // 活動結束日後
            if (act.getActEnd().before(today)) {
                // 將狀態為1(已成團)的活動，改狀態為2(已完成)
                if (act.getActStatus() == 1) {
                    repository.updateActStatus(act.getActNo(), 2);
                }
            }
        }
    }
	
	
	

   
}