package com.topseeker.employee.config;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;


public class MyLocaleResolver implements LocaleResolver{
	
	
	//解析請求
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		//獲取請求中的語言參數
		String language = request.getParameter("l");
		Locale locale = Locale.getDefault();  //沒有就用默認的
		
		if(!StringUtils.isEmpty(language)) {
			String[] split = language.split("_");
			locale = new Locale(split[0], split[1]);
		}
		return locale;
	}
	
	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if (locale != null) {
	        response.setLocale(locale);
	    }
	}

	
}
