package com.topseeker.tour.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Tour3;
import org.springframework.data.domain.Sort;

@Service("tourService")
public class TourService {

	@Autowired
	TourRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addTour(TourVO tourVO) {
		repository.save(tourVO);
	}

	public void updateTour(TourVO tourVO) {
		repository.save(tourVO);
	}

	public void deleteTour(Integer tourNo) {
		if (repository.existsById(tourNo))
			repository.deleteByTourNo(tourNo);
//		    repository.deleteById(tourno);
	}

	public TourVO getOneTour(Integer tourNo) {
		Optional<TourVO> optional = repository.findById(tourNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<TourVO> getAll() {
		return repository.findAll();
	}

	public List<TourVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Tour3.getAllC(map,sessionFactory.openSession());
	}
	
//	===================================
	
	public List<TourVO> getAllToursSortedByPrice() {
        Sort sort = Sort.by(Sort.Direction.ASC, "tourPrice");
        return repository.findAll(sort);
    }
	
	public List<TourVO> getAllToursSorted(Sort sort) {
        return repository.findAll(sort);
    }

}