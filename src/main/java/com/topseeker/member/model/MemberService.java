package com.topseeker.member.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Mem;

@Service("memberService")
public class MemberService {

	@Autowired
	MemberRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addMem(MemberVO memberVO) {
		repository.save(memberVO);
	}

	public void updateMem(MemberVO memberVO) {
		repository.save(memberVO);
	}

	public MemberVO getOneMem(Integer memNo) {
		Optional<MemberVO> optional = repository.findById(memNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	//查詢全部
	public List<MemberVO> getAll() {
		return repository.findAll();
	}
	
	//複合查詢
	public List<MemberVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Mem.getAllC(map,sessionFactory.openSession());
	}
	
	//會員登入
    public Optional<MemberVO> memLogin(String memAccount, String memPassword) {
        return repository.memLogin(memAccount, memPassword);
    }
    
    //會員驗證
    public void verifyMem(Integer memNo) {
    	repository.verifyMem(memNo);
    }
    
    //修改密碼
    public void updatePassword(String memPassword, Integer memNo) {
    	repository.updatePassword(memPassword, memNo);
    }
    
    //修改大頭貼
    public void updateMemImg(byte[] memImg, Integer memNo) {
    	repository.updateMemImg(memImg, memNo);
    }
    
    //忘記密碼
	public Optional<MemberVO> findByEmail(String memEmail) {
        return repository.findByEmail(memEmail);
	}

	//註冊會員時，確認帳號是否重複時使用
	public MemberVO findByAccount(String memAccount) {
		Optional<MemberVO> optional = repository.findByAccount(memAccount);
		return optional.orElse(null);
	}
	
	//使用身分證字號查詢
	public MemberVO findByUid(String memUid) {
		Optional<MemberVO> optional = repository.findByAccount(memUid);
		return optional.orElse(null);
	}
}