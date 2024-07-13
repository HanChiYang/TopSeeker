package com.topseeker.empauth.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmpAuthService {

	@Autowired
	EmpAuthRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addEmpAuth(EmpAuthVO empAuthVO) {
		repository.save(empAuthVO);
	}

//	public void updateEmp(EmpAuthVO empAuthVO) {
//		repository.save(empAuthVO);
//	}

	public void deleteEmpAuth(Integer empNo) {
			repository.deleteEmpAuth(empNo);
	}

//	public EmployeeVO getOneEmp(Integer empno) {
//		Optional<EmployeeVO> optional = repository.findById(empno);
//		return optional.get();
//		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
//	}

	public List<EmpAuthVO> getAll() {
		return repository.findAll();
	}

	public List<EmpAuthVO> takeOneEmpAuth(Integer empNo) {
		return repository.takeOneEmpAuth(empNo);
	}
	
	public void addEmpAuth(Integer empNo, String authNo) {
		repository.addEmpAuth(empNo, Integer.valueOf(authNo));
	}
	
//	public List<EmployeeVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
//	}

}