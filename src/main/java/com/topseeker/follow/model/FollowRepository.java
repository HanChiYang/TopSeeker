package com.topseeker.follow.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FollowRepository extends JpaRepository<FollowVO, Integer> {

	//刪除追隨會員
	@Modifying
	@Query(value = "delete from follow where mem_no =?1 and be_followed_mem_no = ?2", nativeQuery = true)
	void deleteByMemNo(Integer memNo, Integer beFollowedMemNo);

	//查詢是否已有追隨紀錄
	@Query(value = "Select * from follow where mem_no =?1 and be_followed_mem_no = ?2", nativeQuery = true)
	Optional<FollowVO> checkFollowVO(Integer memNo, Integer beFollowedMemNo);
	
	//查詢所有登入會員的追隨者
	@Query(value = "Select * from follow where mem_no=?1", nativeQuery = true)
	List<FollowVO> findAllFollowers(Integer memNo);

	//新增追隨會員
	@Modifying
	@Query(value = "INSERT INTO follow (mem_no, be_followed_mem_no) VALUES (?1, ?2)", nativeQuery = true)
	void addFollow(Integer memNoA, Integer memNoB);
}
