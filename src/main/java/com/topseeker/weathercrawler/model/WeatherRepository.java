package com.topseeker.weathercrawler.model;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherVO, Integer> {
    Optional<WeatherVO> findByLocNameAndWxDate(String locName, Date wxDate);
}

