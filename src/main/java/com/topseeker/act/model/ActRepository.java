package com.topseeker.act.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.follow.model.FollowVO;


@Transactional
public interface ActRepository extends JpaRepository<ActVO, Integer> {

	@Query(value = "from ActVO where memNoA=?1 and actStatus=0 order by actStart")
	List<ActVO> findMyOpenGroup(Integer memNoA);
}
