// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.act.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@Transactional

public interface ActRepository extends JpaRepository<ActVO, Integer> {


	@Query(value = "from ActVO where memNoA=?1 and actStatus=0 order by actStart")
	List<ActVO> findMyOpenGroup(Integer memNoA);

	@Modifying
	@Query(value = "delete from act where act_no =?1", nativeQuery = true)
	void deleteByActNo(int actNo);
	
	@Query(value = "from act where mem_no=?1", nativeQuery = true)
	List<ActVO> findActByMem(Integer memNo);

	
}

