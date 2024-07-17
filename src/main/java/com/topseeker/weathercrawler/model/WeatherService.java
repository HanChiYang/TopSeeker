package com.topseeker.weathercrawler.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.Optional;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    void saveWeatherData(String locName, Date wxDate, String hTemp, String mTemp, String weatherCondition) {
        Optional<WeatherVO> optionalWeatherVO = weatherRepository.findByLocNameAndWxDate(locName, wxDate);
        WeatherVO weatherVO;
        if (optionalWeatherVO.isPresent()) {
            weatherVO = optionalWeatherVO.get();
            if (hTemp != null) {
                weatherVO.sethTemp(hTemp);
            }
            if (mTemp != null) {
                weatherVO.setmTemp(mTemp);
            }
            if (weatherCondition != null) {
            	weatherVO.setWeatherCondition(weatherCondition);
            }
        } else {
            weatherVO = new WeatherVO();
            weatherVO.setLocName(locName);
            weatherVO.setWxDate(wxDate);
            weatherVO.sethTemp(hTemp);
            weatherVO.setmTemp(mTemp);
            weatherVO.setWeatherCondition(weatherCondition);
        }
        weatherRepository.save(weatherVO);
    }
}
