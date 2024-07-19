package com.topseeker.weathercrawler.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.topseeker.weathercrawler.model.WeatherService;


@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    //更新data
    public void saveWeatherData(String locName, Date wxDate, String hTemp, String mTemp, String rainRate, String wX, String uviRate, String uviDesc) {
        
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
            if (rainRate != null) {
            	weatherVO.setRainRate(rainRate);
            }
            if (wX != null) {
            	weatherVO.setwX(wX);
            }
            if (uviRate != null) {
            	weatherVO.setUviRate(uviRate);
            }
            if (uviDesc != null) {
            	weatherVO.setUviDesc(uviDesc);
            }
            
        } else {
            weatherVO = new WeatherVO();
            weatherVO.setLocName(locName);
            weatherVO.setWxDate(wxDate);
            weatherVO.sethTemp(hTemp);
            weatherVO.setmTemp(mTemp);
        	weatherVO.setwX(wX);
        	weatherVO.setUviRate(uviRate);
        	weatherVO.setUviDesc(uviDesc);
        }
        
        weatherRepository.save(weatherVO);
    }
    
    //刪除舊data
    public void truncateWeatherData() {
    	weatherRepository.truncateWeatherData();
    }
    

    //依地點查詢
    public List<WeatherVO> getWxByLoc(String locName) {
    	return weatherRepository.getWxByLoc(locName);
    }


}
