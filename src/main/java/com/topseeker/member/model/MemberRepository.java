// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.member.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

//	@Modifying
//	@Query(value = "delete from member where mem_no =?1", nativeQuery = true)
//	void deleteByMemNo(int memNo);

	//會員登入
	@Query(value = "from MemberVO where memAccount=?1 and memPassword =?2")
    Optional<MemberVO> memLogin(String memAccount, String memPassword);

	//驗證會員狀態
	@Modifying
	@Query(value = "update MemberVO m set m.memStatus = 2 where m.memStatus = 1 and m.memNo=?1")
	void verifyMem(Integer memNo);
	
	//修改密碼
	@Modifying
	@Query(value = "update MemberVO m set m.memPassword = ?1 where m.memNo=?2")
	void updatePassword(String memPassword, Integer memNo);
	
	//修改大頭貼
	@Modifying
	@Query(value = "update MemberVO m set m.memImg = ?1 where m.memNo=?2")
	void updateMemImg(byte[] memImg, Integer memNo);
	
	//忘記密碼，以會員email查找
	@Query(value = "from MemberVO where memEmail=?1")
	Optional<MemberVO> findByEmail(String memEmail);

	//註冊會員時，確認帳號是否重複時使用
	@Query(value = "from MemberVO where memAccount=?1")
	Optional<MemberVO> findByAccount(String Account);
	
	//註冊會員時，確認身分證字號是否重複時使用
	@Query(value = "from MemberVO where memUid=?1")
	Optional<MemberVO> findByUid(String Uid);
}