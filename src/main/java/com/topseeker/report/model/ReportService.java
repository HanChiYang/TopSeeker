package com.topseeker.report.model;
 
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Report;

@Service("reportService")
public class ReportService {
	
	
	@Autowired
	ReportRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addReport(ReportVO reportVO) {
		repository.save(reportVO);
	}

	public void updateReport(ReportVO reportVO) {
		repository.save(reportVO);
	}

	public void deleteReport(Integer actRpNo) {
		if (repository.existsById(actRpNo))
			repository.deleteByActRpNo(actRpNo);
//		    repository.deleteById(actPartNo);
	}

	public ReportVO getOneReport(Integer actRpNo) {
		Optional<ReportVO> optional = repository.findById(actRpNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ReportVO> getAll() {
		return repository.findAll();
	}

	public List<ReportVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Report.getAllC(map,sessionFactory.openSession());
	}
	

}
