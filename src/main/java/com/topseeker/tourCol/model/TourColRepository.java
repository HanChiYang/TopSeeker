// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tourCol.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColVO;
import com.topseeker.tourDetail.model.TourDetailVO;

public interface TourColRepository extends JpaRepository<TourColVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from tour_col where col_no =?1", nativeQuery = true)
	void deleteByColNo(int colNo);

	//● (自訂)條件查詢
//	@Query(value = "from TourColVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<TourColVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
	
	TourColVO findByMemberVOAndTourVO(MemberVO memberVO, TourVO tourVO);
	TourColVO findByColNo(Integer colNo);
	List<TourColVO> findAllByMemberVO(MemberVO memberVO);
	List<TourColVO> findAllByTourVO(TourVO tourVO);
    List<TourColVO> findAllByMemberVOAndTourVO(MemberVO memberVO, TourVO tourVO);

    public interface MemberRepository extends JpaRepository<MemberVO, Integer> {
        MemberVO findByMemAccount(String memAccount);
    }
}