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
    
	//以地點及日期搜尋是否已有資料
	Optional<WeatherVO> findByLocNameAndWxDate(String locName, Date wxDate);

	//每次更新時先刪除表格資料
	@Modifying
	@Query(value = "TRUNCATE TABLE weather", nativeQuery = true)
	void truncateWeatherData();
	
	//依地點查詢資料
	@Query(value = "SELECT * FROM weather WHERE loc_name=?1", nativeQuery = true)
	List<WeatherVO> getWxByLoc(String locName);
	
}

