package com.topseeker.weathercrawler.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import hibernate.util.HibernateUtil_CompositeQuery_sale;

@Service
public class WeatherService {
	
	@Autowired
	WeatherRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addWeather(List<WeatherVO> weatherList) {
		repository.saveAll(weatherList);
	}

	public void deleteWeather() {
		repository.deleteWeather();
	}

	public List<WeatherVO> getAll(){
		return repository.findAll();
	}
	
    public void saveWeatherDataFromApi(List<WeatherVO> weatherDataList) {
        repository.saveAll(weatherDataList);
    }
}
