package com.topseeker.news.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import com.topseeker.act.model.ActVO;
import com.topseeker.news.model.NewsService;
import com.topseeker.news.model.NewsVO;
import com.topseeker.news.model.RedisService;

@Controller
@Validated
@RequestMapping("/news")
public class NewsnoController {
	
	@Autowired
	NewsService newsSvc;
	
	@Autowired
    RedisService redisService;
	
	@GetMapping("newsDetails")
    public String getNewsDetails(@RequestParam("newsNo") String newsNo, HttpSession session, Model model) {
		NewsVO newsVO = newsSvc.getOneNews(Integer.valueOf(newsNo));
		
		// 檢查此新聞是否已在此 session 中被查看
	    String sessionKey = "viewedNews_" + newsNo;
	    if (session.getAttribute(sessionKey) == null) {
	        // 如果沒有被查看過，增加觀看次數
	        redisService.incrementViewCount(Integer.valueOf(newsNo));
	        // 將此新聞標記為已查看
	        session.setAttribute(sessionKey, true);
	    }
        // 獲取觀看次數並添加到模型中
        Integer viewCount = redisService.getViewCount(Integer.valueOf(newsNo));
        model.addAttribute("viewCount", viewCount);
        
        model.addAttribute("newsVO", newsVO);
        return "front-end/news/listOneNews";
    }
	
}