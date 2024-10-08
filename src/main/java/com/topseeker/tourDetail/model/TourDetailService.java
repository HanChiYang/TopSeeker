package com.topseeker.tourDetail.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.tour.model.TourVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_TourDetail3;


@Service
public class TourDetailService {

    @Autowired
    TourDetailRepository repository;
    
	@Autowired
    private SessionFactory sessionFactory;
	
	
	
	
	
	public TourDetailVO getByTourNoAndDetailDay(TourVO tourVO, Integer detailDay) {
	    // 在這裡實現查詢邏輯，例如使用 JPA Repository 查詢
	    return repository.findByTourVOAndDetailDay(tourVO, detailDay);
	}
	
	
	public TourDetailVO getByDetailNo(Integer detailNo) {
		Optional<TourDetailVO> optional = repository.findById(detailNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	
	
    
    public List<TourDetailVO> getAll() {
        return repository.findAll();
    }
//    public List<TourDetailVO> getAllNo(Map<String, String[]> map) {
//        return repository.selectTourNo(map);
//    }
    
    public List<TourDetailVO> getAllByCompositeKey(TourVO tourVO, Integer detailDay) {
        return repository.findAllByTourVOAndDetailDay(tourVO, detailDay);
    }
    
    public Boolean existsByTourVOAndDetailDay(TourVO tourVO, Integer detailDay) {
        return repository.findAllByTourVOAndDetailDay(tourVO, detailDay).size() > 0;
    }

    public List<TourDetailVO> findByOthers(Integer tourNo) {
        return repository.findByOthers(tourNo);
    }
    public List<TourDetailVO> getByTourNo(TourVO tourVO) {
        return repository.findAllByTourVO(tourVO);
    }
   

    public TourDetailVO addTourDetail(TourDetailVO tourDetail) {
        return repository.save(tourDetail);
    }

    public TourDetailVO updateTourDetail(TourDetailVO tourDetail) {
        return repository.save(tourDetail);
    }

    public void deleteByDetailNo(Integer detailNo) {
		if (repository.existsById(detailNo))
			repository.deleteByDetailNo(detailNo);
//		    repository.deleteById(tourno);
	}
    public void deleteTourDetail(Integer detailNo) {
		 repository.deleteById(detailNo);
	}
    
	public List<TourDetailVO> getAll(Map<String, String[]> map) {
	return HibernateUtil_CompositeQuery_TourDetail3.getAllC(map,sessionFactory.openSession());
	}
	
	public List<TourDetailVO> findDetailsByTourNo(Integer tourNo) {
        return repository.findByTourVO_TourNo(tourNo);
    }
	

	public void add(TourDetailVO tourDetail) {
        boolean exists = repository.existsByTourVO_TourNoAndDetailDay(
            tourDetail.getTourVO().getTourNo(), tourDetail.getDetailDay());
        if (exists) {
            throw new DuplicateTourDetailException("行程: " + tourDetail.getTourVO().getTourName() + "，第" + tourDetail.getDetailDay() +" 日 "+ " 已經存在。");
        }
        repository.save(tourDetail);
    }
	
	
	public class DuplicateTourDetailException extends RuntimeException {
	    public DuplicateTourDetailException(String message) {
	        super(message);
	    }
	}

}



