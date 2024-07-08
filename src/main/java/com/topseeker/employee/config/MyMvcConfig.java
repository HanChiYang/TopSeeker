package com.topseeker.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/emp/select_page.html").setViewName("login");
    }
	
	//自訂義的國際化就可以生效了
	@Bean
	public LocaleResolver localeResolver() {
		return new MyLocaleResolver();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new LoginHandlerInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/", "/login", "/login.html", "/images/**", "/css/**", "/js/**", "/resources/**");
	}
}
