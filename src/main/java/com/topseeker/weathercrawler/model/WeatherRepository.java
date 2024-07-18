package com.topseeker.weathercrawler.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface WeatherRepository extends JpaRepository<WeatherVO, Integer>{
	
	@Modifying
	@Query(value = "DELETE FROM weather", nativeQuery = true)
	void deleteWeather();
	
	
}
