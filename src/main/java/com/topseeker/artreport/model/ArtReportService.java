package com.topseeker.artreport.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_ArtReport;

@Service("artreportService")
public class ArtReportService {
	
	@Autowired
	ArtReportRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addArtReport(ArtReportVO artreportVO) {
		repository.save(artreportVO);
	}

	public void updateArtReport(ArtReportVO artreportVO) {
		repository.save(artreportVO);
	}

	public void deleteArtReport(Integer artReportNo) {
		if (repository.existsById(artReportNo))
			repository.deleteByArtReportNo(artReportNo);
//		    repository.deleteById(actPartNo);
	}

	public ArtReportVO getOneArtReport(Integer artReportNo) {
		Optional<ArtReportVO> optional = repository.findById(artReportNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArtReportVO> getAll() {
		return repository.findAll();
	}

	public List<ArtReportVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_ArtReport.getAllC(map,sessionFactory.openSession());
	}


}
