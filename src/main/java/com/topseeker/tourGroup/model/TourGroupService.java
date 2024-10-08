package com.topseeker.tourGroup.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_TourGroup3;


@Service("tourGroupService")
public class TourGroupService {

	@Autowired
	TourGroupRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public List<TourGroupVO> findGroupsByTourNo(Integer tourNo) {
        return repository.findByTourVO_TourNo(tourNo);
    }
	public void addTourGroup(TourGroupVO tourGroupVO) {
		repository.save(tourGroupVO);
	}

	public void updateTourGroup(TourGroupVO tourGroupVO) {
		repository.save(tourGroupVO);
	}

	public void deleteGroupNo(Integer groupNo) {
		if (repository.existsById(groupNo))
			repository.deleteByGroupNo(groupNo);
//		    repository.deleteById(groupNo);
	}

	public TourGroupVO getOneTourGroup(Integer groupNo) {
		Optional<TourGroupVO> optional = repository.findById(groupNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<TourGroupVO> getAll() {
		return repository.findAll();
	}

	public List<TourGroupVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_TourGroup3.getAllC(map,sessionFactory.openSession());
	}
	
	
	
	public void updateGroupBal(Integer groupNo, Integer orderNums) {
        Optional<TourGroupVO> optional = repository.findById(groupNo);
        if (optional.isPresent()) {
            TourGroupVO tourGroupVO = optional.get();
            int newBal = tourGroupVO.getGroupBal() - orderNums;
            if (newBal < 1) {
                throw new IllegalArgumentException("更新后的名額數不能小於1");
            }
            tourGroupVO.setGroupBal(newBal);
            repository.save(tourGroupVO);
        } else {
            throw new EntityNotFoundException("行程未找到");
        }
    }
}

