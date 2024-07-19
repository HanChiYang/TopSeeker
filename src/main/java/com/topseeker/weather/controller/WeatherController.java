package com.topseeker.weather.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.topseeker.weathercrawler.model.WeatherService;
import com.topseeker.weathercrawler.model.WeatherVO;

@Controller
@RequestMapping("/weather")
public class WeatherController {
	
	@Autowired
	WeatherService weatherSvc;
	
	//天氣查詢頁面
    @GetMapping
	public String weatherPage(Model model) {
		return "front-end/weather/weather";
	}
    
    //查詢天氣
    @GetMapping("/ajexsearch")
    public String weatherAjexSearch(@RequestParam("locName") String locName, Model model) {
    	List<WeatherVO> weatherVOList = weatherSvc.getWxByLoc(locName);
    	model.addAttribute("weatherVOList", weatherVOList);
    	model.addAttribute("locName", locName);
    	return "front-end/weather/search :: weatherVOList";
    }
     
}