package com.topseeker.tourCol.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColVO;
import com.topseeker.tourCol.model.TourColVO;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_TourCol3;


@Service("tourColService")
public class TourColService {

	@Autowired
	TourColRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addTourCol(TourColVO tourColVO) {
		repository.save(tourColVO);
	}

	public void updateTourCol(TourColVO tourColVO) {
		repository.save(tourColVO);
	}

	public void deleteCol(Integer colNo) {
		if (repository.existsById(colNo))
			repository.deleteByColNo(colNo);
//		    repository.deleteById(colNo);
	}

	public TourColVO getOneTourCol(Integer colNo) {
		Optional<TourColVO> optional = repository.findById(colNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public List<TourColVO> getByTourNo(TourVO tourVO) {
        return repository.findAllByTourVO(tourVO);
    }
	
	
	
	public TourColVO getByMemberVOAndTourVo(MemberVO memberVO, TourVO tourVO) {
	    // 在這裡實現查詢邏輯，例如使用 JPA Repository 查詢
	    return repository.findByMemberVOAndTourVO(memberVO,  tourVO);
	}
	
	public List<TourColVO> getAll() {
		return repository.findAll();
	}
	
	
    public List<TourColVO> getByMemNoAndTourNo(MemberVO memberVO, TourVO tourVO) {
	    // 在這裡實現查詢邏輯，例如使用 JPA Repository 查詢
	    return repository.findAllByMemberVOAndTourVO(memberVO, tourVO);
	}
    
    public List<TourColVO> getByMemNo(MemberVO memberVO) {
        return repository.findAllByMemberVO(memberVO);
    }
    
    
//    =========登入必須有此方法=======
//    public MemberVO findMemberByUsername(String username) {
//        return memberRepository.findByUsername(username);
//    }
    
    

//	public List<TourColVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_TourCol3.getAllC(map,sessionFactory.openSession());
//	}

}