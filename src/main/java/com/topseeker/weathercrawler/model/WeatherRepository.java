package com.topseeker.weathercrawler.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WeatherRepository extends JpaRepository<WeatherVO, Integer> {
    
	Optional<WeatherVO> findByLocNameAndWxDate(String locName, Date wxDate);

	@Modifying
	@Query(value = "TRUNCATE TABLE weather", nativeQuery = true)
	void truncateWeatherData();
	
	@Query(value = "SELECT DISTINCT loc_name FROM weather", nativeQuery = true)
	List<String> getPlace();
	
	@Query(value = "SELECT * FROM weather WHERE loc_name=?1", nativeQuery = true)
	List<WeatherVO> getWxByLoc(String locName);
	
	
}

