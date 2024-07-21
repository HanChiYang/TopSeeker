// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.tourCol.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.follow.model.FollowVO;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColVO;
import com.topseeker.tourDetail.model.TourDetailVO;

@Transactional
public interface TourColRepository extends JpaRepository<TourColVO, Integer> {

	@Modifying
	@Query(value = "delete from tour_col where mem_no=?1 and tour_no =?2", nativeQuery = true)
	void delete(int memNo,int tourNo);

	//● (自訂)條件查詢
//	@Query(value = "from TourColVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<TourColVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
	
	TourColVO findByMemberVOAndTourVO(MemberVO memberVO, TourVO tourVO);
	TourColVO findByColNo(Integer colNo);
	List<TourColVO> findAllByMemberVO(MemberVO memberVO);
	List<TourColVO> findAllByTourVO(TourVO tourVO);
    List<TourColVO> findAllByMemberVOAndTourVO(MemberVO memberVO, TourVO tourVO);

    //查詢是否已有收藏
  	@Query(value = "Select * from tour_col where mem_no =?1 and tour_no = ?2", nativeQuery = true)
  	Optional<TourColVO> checkTourColVO(Integer memNo, Integer tourNo);
  	
  	//查詢所有登入會員的收藏
  	@Query(value = "Select * from tour_col where mem_no=?1", nativeQuery = true)
  	List<TourColVO> findAllTourCols(Integer memNo);
  	
    //刪除追隨會員
  	@Modifying
  	@Query(value = "delete from tour_col where mem_no =?1 and tour_no = ?2", nativeQuery = true)
  	void deleteByMemNo(Integer memNo, Integer tourNo);
  	
    //新增追隨會員
  	@Modifying
  	@Query(value = "INSERT INTO tour_col (mem_no, tour_no) VALUES (?1, ?2)", nativeQuery = true)
  	void addTourCol(Integer memNo, Integer tourNo);
  	
}