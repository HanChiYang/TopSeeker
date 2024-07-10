package com.topseeker.follow.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.follow.model.FollowVO;

 

public interface FollowRepository extends JpaRepository<FollowVO, Integer>  {
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from follow where mem_no =?1 and be_followed_mem_no = ?2 ", nativeQuery = true)
	void deleteByMemNoA(int memNoA,int memNoB);

	//● (自訂)條件查詢
	@Query(value = "from FollowVO where mem_no=?1  order by mem_no")
	List<FollowVO> findByOthers(int memNoA);


}
