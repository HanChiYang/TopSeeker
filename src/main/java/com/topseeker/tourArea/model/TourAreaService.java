package com.topseeker.tourArea.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.tour.model.TourVO;

@Service("tourAreaService")
public class TourAreaService {

	@Autowired
	TourAreaRepository repository;

	public void addTourArea(TourAreaVO tourAreaVO) {
		repository.save(tourAreaVO);
	}

	public void updateTourArea(TourAreaVO tourAreaVO) {
		repository.save(tourAreaVO);
	}

	public void deleteTourArea(Integer areaNo) {
		if (repository.existsById(areaNo))
			repository.deleteById(areaNo);
	}


	public TourAreaVO getOneTourArea(Integer areaNo) {
		Optional<TourAreaVO> optional = repository.findById(areaNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<TourAreaVO> getAll() {
		return repository.findAll();
	}

	public Set<TourVO> getToursByAreaNo(Integer areaNo) {
		return getOneTourArea(areaNo).getTours();
	}

}
