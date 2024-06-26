// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.member.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	@Modifying
	@Query(value = "delete from member where mem_no =?1", nativeQuery = true)
	void deleteByMemNo(int memNo);

	//會員登入
	@Query(value = "from MemberVO where memAccount=?1 and memPassword =?2")
    Optional<MemberVO> memLogin(String memAccount, String memPassword);
}