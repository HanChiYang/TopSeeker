package com.topseeker.employee.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//
//import com.topseeker.report.model.ReportRepository;
//import com.topseeker.report.model.ReportVO;
import com.topseeker.employee.model.EmployeeVO;
import com.topseeker.member.model.MemberRepository;
import com.topseeker.member.model.MemberVO;
//import com.topseeker.participant.model.ParticipantVO;
//
//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Report;
@Service("EmployeeService")
public class EmployeeService {
	
	@Autowired
	EmployeeRepository repository;

	public void addEmployee(EmployeeVO employeeVO) {
		repository.save(employeeVO);
	}

	public void updateEmployee(EmployeeVO employeeVO) {
		repository.save(employeeVO);
	}

	public void deleteEmployee(Integer empNo) {
		if (repository.existsById(empNo))
			repository.deleteById(empNo);
	}


	public EmployeeVO getOneEmployee(Integer empNo) {
		Optional<EmployeeVO> optional = repository.findById(empNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<EmployeeVO> getAll() {
		return repository.findAll();
	}

//	public Set<ReportVO> getReportByActRpNo(Integer actRpNo) {
//		return getOneEmployee(actRpNo).getReports();
//	}
	

}
